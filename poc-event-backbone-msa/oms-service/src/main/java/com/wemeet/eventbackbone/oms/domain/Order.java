package com.wemeet.eventbackbone.oms.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 주문(운송 오더) 애그리거트 — 오더 생애주기 상태의 <b>단일 권위(authority)</b>.
 * 상태 전이는 이 애그리거트가 가드한다(유효할 때만). <b>낙관적 잠금(@Version)</b>으로 동시 전이를 직렬화 —
 * 배차확정(→DISPATCHED)과 오더취소(→CANCELLED)가 동시에 와도 하나만 성립하고 나머지는 현재 상태로 판정된다.
 * 상태: CREATED → DISPATCHED → DELIVERED → SETTLED · CREATED→CANCELLED · DISPATCHED→CREATED(배차취소 복귀).
 */
@Table("orders")
public record Order(
        @Id @Column("order_id") String orderId,
        @Column("shipper_id") String shipperId,
        @Column("origin") String origin,
        @Column("destination") String destination,
        @Column("amount") String amount,
        @Column("currency") String currency,
        @Column("status") String status,
        @Version @Column("version") Long version
) {

    public static final String CREATED = "CREATED";
    public static final String DISPATCHED = "DISPATCHED";
    public static final String DELIVERED = "DELIVERED";
    public static final String SETTLED = "SETTLED";
    public static final String CANCELLED = "CANCELLED";

    public static Order create(String orderId, String shipperId, String origin, String destination,
                               String amount, String currency) {
        return new Order(orderId, shipperId, origin, destination, amount, currency, CREATED, null);
    }

    /** 상태만 바꾼 새 인스턴스(version 유지 → save 시 낙관적 잠금 검사·증가). */
    public Order withStatus(String newStatus) {
        return new Order(orderId, shipperId, origin, destination, amount, currency, newStatus, version);
    }
}
