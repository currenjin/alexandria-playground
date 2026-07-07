package com.wemeet.eventbackbone.bms.domain;

public record Settlement(String settlementId, String tripId, String amount, String status) {
    public static final String SCHEDULED = "SCHEDULED";
}
