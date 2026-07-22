package com.wemeet.oms.application;

import com.wemeet.core.event.consume.EventHandler;
import com.wemeet.core.event.publish.EventPublisher;
import com.wemeet.contract.OrderContracts.DeliverOrder;
import com.wemeet.contract.OrderContracts.DispatchOrder;
import com.wemeet.contract.OrderContracts.OrderCancelRejected;
import com.wemeet.contract.OrderContracts.OrderCancelled;
import com.wemeet.contract.OrderContracts.OrderCreated;
import com.wemeet.contract.OrderContracts.OrderDelivered;
import com.wemeet.contract.OrderContracts.OrderDispatchRejected;
import com.wemeet.contract.OrderContracts.OrderDispatched;
import com.wemeet.contract.OrderContracts.OrderSettled;
import com.wemeet.contract.OrderContracts.OrderUndispatched;
import com.wemeet.contract.OrderContracts.SettleOrder;
import com.wemeet.contract.OrderContracts.UndispatchOrder;
import com.wemeet.oms.domain.Order;
import com.wemeet.oms.domain.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 오더 유스케이스 — 오더 생애주기의 <b>단일 권위</b>. 모든 상태 전이를 가드하고, 낙관적 잠금(@Version)으로
 * 동시 전이를 직렬화한다. 사가 커맨드(oms.cmd.*)는 "전이 시도"이고, OMS가 현재 상태로 판정해
 * <b>성공 사실</b>(OrderDispatched 등)이나 <b>거부 사실</b>(OrderDispatchRejected)을 발행한다.
 * "배차된 오더는 취소 불가" 규칙도 여기서(권위 상태로) 판정 → 창 없이 지켜진다.
 */
@Service
public class OmsService {

    private static final Logger log = LoggerFactory.getLogger(OmsService.class);
    private static final Map<String, Integer> RANK =
            Map.of(Order.CREATED, 0, Order.DISPATCHED, 1, Order.DELIVERED, 2, Order.SETTLED, 3);

    private final OrderRepository orders;
    private final EventPublisher events;

    public OmsService(OrderRepository orders, EventPublisher events) {
        this.orders = orders;
        this.events = events;
    }

    /** 오더 생성(진입점, 로컬 TX). */
    @Transactional
    public void create(String orderId, String shipperId, String origin, String destination,
                       String amount, String currency) {
        orders.save(Order.create(orderId, shipperId, origin, destination, amount, currency));
        events.publish(new OrderCreated(orderId, shipperId, origin, destination, amount, currency));
        log.info("오더 생성 {} {}→{} amount={}", orderId, origin, destination, amount);
    }

    /** 오더 취소(사용자 액션, 로컬 TX) — CREATED만 취소, 배차 이후면 규칙상 거부. 낙관적 잠금으로 배차확정과 직렬화. */
    @Transactional
    public void cancelOrder(String orderId, String reason) {
        Order o = orders.findById(orderId).orElse(null);
        if (o == null) { log.info("오더 취소 대상 없음 {}", orderId); return; }
        if (Order.CREATED.equals(o.status())) {
            orders.save(o.withStatus(Order.CANCELLED));
            events.publish(new OrderCancelled(orderId, reason));
            log.info("오더 취소 {} 사유={}", orderId, reason);
        } else {
            events.publish(new OrderCancelRejected(orderId, reason, o.status()));
            log.info("오더 취소 거부 {} — 현재 {} (배차된 오더는 취소 불가)", orderId, o.status());
        }
    }

    // ── 사가 커맨드: 가드된 전이(성공/거부 사실 발행) ──

    /** 배차 확정 반영: CREATED→DISPATCHED. 불가(취소·정산 등)면 거부 → 배차확정 사가가 보상. */
    @EventHandler
    void onDispatchOrder(DispatchOrder cmd) {
        Order o = load(cmd.orderId());
        if (Order.CREATED.equals(o.status())) {
            orders.save(o.withStatus(Order.DISPATCHED));
            events.publish(new OrderDispatched(cmd.orderId(), cmd.dispatchId()));
            log.info("오더 배차됨 {} (dispatch={})", cmd.orderId(), cmd.dispatchId());
        } else if (Order.DISPATCHED.equals(o.status())) {
            events.publish(new OrderDispatched(cmd.orderId(), cmd.dispatchId()));   // 멱등 재통지
        } else {
            events.publish(new OrderDispatchRejected(cmd.orderId(), cmd.dispatchId(), o.status()));
            log.info("배차 반영 거부 {} — 현재 {} → 보상(배차취소) 유도", cmd.orderId(), o.status());
        }
    }

    /** 미배차 복귀(배차취소 사가): DISPATCHED→CREATED. 아니면 무시(보상으로 온 것 등). */
    @EventHandler
    void onUndispatchOrder(UndispatchOrder cmd) {
        Order o = orders.findById(cmd.orderId()).orElse(null);
        if (o != null && Order.DISPATCHED.equals(o.status())) {
            orders.save(o.withStatus(Order.CREATED));
            events.publish(new OrderUndispatched(cmd.orderId()));
            log.info("오더 미배차 복귀 {}", cmd.orderId());
        } else {
            log.info("미배차 복귀 무시 {} — 현재 {} (배차 상태 아님)", cmd.orderId(),
                    o == null ? "없음" : o.status());
        }
    }

    /** 배송 완료 반영: DISPATCHED→DELIVERED. 아직 배차 전이면 재시도(순서 역전 self-heal). */
    @EventHandler
    void onDeliverOrder(DeliverOrder cmd) {
        Order o = load(cmd.orderId());
        if (Order.DISPATCHED.equals(o.status())) {
            orders.save(o.withStatus(Order.DELIVERED));
            events.publish(new OrderDelivered(cmd.orderId(), cmd.dispatchId(), o.amount()));
            log.info("오더 배송완료 {} amount={}", cmd.orderId(), o.amount());
        } else if (Order.DELIVERED.equals(o.status()) || Order.SETTLED.equals(o.status())) {
            events.publish(new OrderDelivered(cmd.orderId(), cmd.dispatchId(), o.amount()));   // 멱등
        } else {
            requireCaughtUp(o, Order.DISPATCHED, cmd.orderId(), "DeliverOrder");
            log.info("배송완료 반영 무시 {} — 현재 {}", cmd.orderId(), o.status());
        }
    }

    /** 정산 완료 반영: DELIVERED→SETTLED. 아직이면 재시도(순서 역전 self-heal). */
    @EventHandler
    void onSettleOrder(SettleOrder cmd) {
        Order o = load(cmd.orderId());
        if (Order.DELIVERED.equals(o.status())) {
            orders.save(o.withStatus(Order.SETTLED));
            events.publish(new OrderSettled(cmd.orderId(), cmd.settlementId()));
            log.info("오더 정산완료 {} (settlement={})", cmd.orderId(), cmd.settlementId());
        } else if (Order.SETTLED.equals(o.status())) {
            events.publish(new OrderSettled(cmd.orderId(), cmd.settlementId()));   // 멱등
        } else {
            requireCaughtUp(o, Order.DELIVERED, cmd.orderId(), "SettleOrder");
            log.info("정산완료 반영 무시 {} — 현재 {}", cmd.orderId(), o.status());
        }
    }

    private Order load(String orderId) {
        return orders.findById(orderId).orElseThrow(() -> new IllegalStateException(
                "[order " + orderId + "] 아직 없음(OrderCreated 미처리) — 재시도 대기"));
    }

    /** 현재 상태가 기대 이전(프로세스가 아직 못 따라옴)이면 무시 말고 재시도(백오프 self-heal). */
    private void requireCaughtUp(Order o, String expectedPrior, String orderId, String evt) {
        if (RANK.getOrDefault(o.status(), 99) < RANK.get(expectedPrior)) {
            throw new IllegalStateException("[order " + orderId + "] 상태 " + o.status()
                    + "(기대 " + expectedPrior + " 이전) — " + evt + " 재시도 대기");
        }
    }
}
