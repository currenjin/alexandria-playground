package com.wemeet.eventbackbone.bms.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("settlements")
public record Settlement(
        @Id @Column("settlement_id") String settlementId,
        @Column("trip_id") String tripId,
        @Column("amount") String amount,
        @Column("status") String status
) implements Persistable<String> {

    public static final String SCHEDULED = "SCHEDULED";

    @Override
    public String getId() {
        return settlementId;
    }

    @Override
    public boolean isNew() {
        return true;
    }
}
