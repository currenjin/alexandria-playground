package com.wemeet.eventbackbone.tms.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/** 배차 도메인 엔티티. 상태: DISPATCHED → DELIVERED, 그리고 CANCELLED(배송 전만). */
@Table("trips")
public record Trip(
        @Id @Column("trip_id") String tripId,
        @Column("order_id") String orderId,
        @Column("carrier_id") String carrierId,
        @Column("status") String status
) implements Persistable<String> {

    public static final String DISPATCHED = "DISPATCHED";
    public static final String DELIVERED = "DELIVERED";
    public static final String CANCELLED = "CANCELLED";

    @Override
    public String getId() {
        return tripId;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
