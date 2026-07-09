package com.wemeet.eventbackbone.oms.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 주문(운송 오더) 도메인 엔티티 — 루티프로 미들마일: 1상차(origin)→1하차(destination).
 * Spring Data JDBC 매핑. save()는 신규 INSERT 전용(isNew()=true), 상태 변경은 리포지토리 명시 update.
 * 상태: CREATED → DISPATCHED → DELIVERED → SETTLED, 그리고 CANCELLED(CREATED에서만).
 */
@Table("orders")
public record Order(
        @Id @Column("order_id") String orderId,
        @Column("shipper_id") String shipperId,
        @Column("origin") String origin,
        @Column("destination") String destination,
        @Column("amount") String amount,
        @Column("currency") String currency,
        @Column("status") String status
) implements Persistable<String> {

    public static final String CREATED = "CREATED";
    public static final String DISPATCHED = "DISPATCHED";
    public static final String DELIVERED = "DELIVERED";
    public static final String SETTLED = "SETTLED";
    public static final String CANCELLED = "CANCELLED";

    public static Order create(String orderId, String shipperId, String origin, String destination,
                               String amount, String currency) {
        return new Order(orderId, shipperId, origin, destination, amount, currency, CREATED);
    }

    @Override
    public String getId() {
        return orderId;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
