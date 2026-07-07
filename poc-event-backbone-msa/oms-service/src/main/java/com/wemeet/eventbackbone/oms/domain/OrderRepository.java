package com.wemeet.eventbackbone.oms.domain;

/** 리포지토리 포트 (domain 레이어). 구현은 infrastructure. */
public interface OrderRepository {
    void save(Order order);
    void updateStatus(String orderId, String status);
}
