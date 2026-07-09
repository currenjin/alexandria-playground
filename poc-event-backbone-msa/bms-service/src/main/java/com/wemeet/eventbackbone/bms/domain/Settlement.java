package com.wemeet.eventbackbone.bms.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/** 정산 도메인 엔티티. 배송 완료된 배차의 운임 정산(즉시 COMPLETED). */
@Table("settlements")
public record Settlement(
        @Id @Column("settlement_id") String settlementId,
        @Column("dispatch_id") String dispatchId,
        @Column("order_id") String orderId,
        @Column("amount") String amount,
        @Column("status") String status
) implements Persistable<String> {

    public static final String COMPLETED = "COMPLETED";

    @Override
    public String getId() {
        return settlementId;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
