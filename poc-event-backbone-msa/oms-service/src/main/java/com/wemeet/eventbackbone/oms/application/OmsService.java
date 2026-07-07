package com.wemeet.eventbackbone.oms.application;

import com.wemeet.eventbackbone.common.event.EventPublisher;
import com.wemeet.eventbackbone.common.event.HandlerRegistry;
import com.wemeet.eventbackbone.contracts.OrderContracts.CancelOrder;
import com.wemeet.eventbackbone.contracts.OrderContracts.OrderCancelled;
import com.wemeet.eventbackbone.contracts.OrderContracts.OrderConfirmed;
import com.wemeet.eventbackbone.oms.domain.Order;
import com.wemeet.eventbackbone.oms.domain.OrderRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 애플리케이션 서비스 (application 레이어). 유스케이스: 주문 확정 / 취소(보상).
 * 사가를 모른다 — 커맨드 핸들러(step)만 등록.
 */
@Service
public class OmsService {

    private static final Logger log = LoggerFactory.getLogger(OmsService.class);

    private final OrderRepository orders;
    private final EventPublisher events;
    private final HandlerRegistry registry;

    public OmsService(OrderRepository orders, EventPublisher events, HandlerRegistry registry) {
        this.orders = orders;
        this.events = events;
        this.registry = registry;
    }

    @PostConstruct
    void register() {
        registry.register("oms", CancelOrder.class, this::onCancelOrder);   // 보상 커맨드 처리
    }

    /** 주문 확정 — 도메인 저장 + publish가 같은 트랜잭션(§7.1.3). */
    @Transactional
    public void confirm(String orderId, String customerId, String amount, String currency) {
        orders.save(Order.confirm(orderId, customerId, amount, currency));
        events.publish(new OrderConfirmed(orderId, customerId, amount, currency));
        log.info("주문 확정 {} amount={}", orderId, amount);
    }

    /** 보상: 사가가 CancelOrder 커맨드를 보냄. */
    void onCancelOrder(CancelOrder cmd) {
        orders.updateStatus(cmd.orderId(), Order.CANCELLED);
        events.publish(new OrderCancelled(cmd.orderId(), cmd.reason()));
        log.info("주문 취소(보상) {} 사유={}", cmd.orderId(), cmd.reason());
    }
}
