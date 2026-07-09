package com.wemeet.eventbackbone.oms.application;

import com.wemeet.eventbackbone.common.event.EventHandler;
import com.wemeet.eventbackbone.common.event.EventPublisher;
import com.wemeet.eventbackbone.contracts.OrderContracts.MarkDelivered;
import com.wemeet.eventbackbone.contracts.OrderContracts.MarkDispatched;
import com.wemeet.eventbackbone.contracts.OrderContracts.MarkSettled;
import com.wemeet.eventbackbone.contracts.OrderContracts.MarkUndispatched;
import com.wemeet.eventbackbone.contracts.OrderContracts.OrderCancelRejected;
import com.wemeet.eventbackbone.contracts.OrderContracts.OrderCancelled;
import com.wemeet.eventbackbone.contracts.OrderContracts.OrderCreated;
import com.wemeet.eventbackbone.oms.domain.Order;
import com.wemeet.eventbackbone.oms.domain.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 주문 유스케이스. 오더 생성·취소는 OMS 자기 API(OrderController)로 직접 수행하고,
 * 오더 상태 전이(배차/배송/정산/미배차 복귀)는 orchestrator가 보내는 커맨드(oms.cmd.*)를
 * {@code @EventHandler}로 받아 동기화한다. "배차되면 취소 불가"는 애그리거트 불변식으로 여기서 판정한다.
 */
@Service
public class OmsService {

    private static final Logger log = LoggerFactory.getLogger(OmsService.class);

    private final OrderRepository orders;
    private final EventPublisher events;

    public OmsService(OrderRepository orders, EventPublisher events) {
        this.orders = orders;
        this.events = events;
    }

    /** 오더 생성(진입점). → OrderCreated 로 orchestrator 프로세스가 시작된다. */
    @Transactional
    public void create(String orderId, String shipperId, String origin, String destination,
                       String amount, String currency) {
        orders.save(Order.create(orderId, shipperId, origin, destination, amount, currency));
        events.publish(new OrderCreated(orderId, shipperId, origin, destination, amount, currency));
        log.info("오더 생성 {} {}→{} amount={}", orderId, origin, destination, amount);
    }

    /** 오더 취소(진입점) — 불변식 판정: CREATED만 취소 가능. 배차 이후면 거부(규칙). */
    @Transactional
    public void cancelOrder(String orderId, String reason) {
        String status = orders.findById(orderId).map(Order::status).orElse(null);
        if (Order.CREATED.equals(status)) {
            orders.updateStatus(orderId, Order.CANCELLED);
            events.publish(new OrderCancelled(orderId, reason));
            log.info("오더 취소 {} 사유={}", orderId, reason);
        } else {
            events.publish(new OrderCancelRejected(orderId,
                    "배차 이후 상태(" + status + ")는 취소 불가", status));
            log.info("오더 취소 거부 {} — 현재 {} (배차된 오더는 취소 불가)", orderId, status);
        }
    }

    // ── orchestrator 커맨드 소비 (오더 상태 동기화) ──
    @EventHandler
    void onMarkDispatched(MarkDispatched cmd) {
        orders.updateStatus(cmd.orderId(), Order.DISPATCHED);
        log.info("오더 배차됨 {} (dispatch={})", cmd.orderId(), cmd.dispatchId());
    }

    @EventHandler
    void onMarkDelivered(MarkDelivered cmd) {
        orders.updateStatus(cmd.orderId(), Order.DELIVERED);
        log.info("오더 배송완료 {}", cmd.orderId());
    }

    @EventHandler
    void onMarkSettled(MarkSettled cmd) {
        orders.updateStatus(cmd.orderId(), Order.SETTLED);
        log.info("오더 정산완료 {} (settlement={})", cmd.orderId(), cmd.settlementId());
    }

    @EventHandler
    void onMarkUndispatched(MarkUndispatched cmd) {
        orders.updateStatus(cmd.orderId(), Order.CREATED);
        log.info("오더 미배차 복귀 {}", cmd.orderId());
    }
}
