package com.wemeet.eventbackbone.oms.domain;

/** 도메인 엔티티 (domain 레이어). */
public record Order(String orderId, String customerId, String amount, String currency, String status) {

    public static final String CONFIRMED = "CONFIRMED";
    public static final String CANCELLED = "CANCELLED";

    public static Order confirm(String orderId, String customerId, String amount, String currency) {
        return new Order(orderId, customerId, amount, currency, CONFIRMED);
    }
}
