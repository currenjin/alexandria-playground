package com.wemeet.oms.domain;

import java.util.Optional;

/** 리포지토리 포트 (domain 레이어). 전이는 load→withStatus→save(낙관적 잠금). */
public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(String orderId);
}
