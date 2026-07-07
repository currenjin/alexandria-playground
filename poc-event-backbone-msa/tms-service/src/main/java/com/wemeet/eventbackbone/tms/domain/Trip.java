package com.wemeet.eventbackbone.tms.domain;

public record Trip(String tripId, String orderId, String carrierId, String status) {
    public static final String DISPATCHED = "DISPATCHED";
    public static final String CANCELLED = "CANCELLED";
}
