package com.wemeet.eventbackbone.oms.infrastructure;

import com.wemeet.eventbackbone.oms.domain.Order;
import com.wemeet.eventbackbone.oms.domain.OrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/** 도메인 포트 어댑터 — Spring Data JDBC에 위임. save는 @Version 낙관적 잠금(충돌 시 예외→소비 재시도). */
@Repository
public class SpringDataOrderRepository implements OrderRepository {

    private final OrderCrudRepository crud;

    public SpringDataOrderRepository(OrderCrudRepository crud) {
        this.crud = crud;
    }

    @Override
    public void save(Order order) {
        crud.save(order);
    }

    @Override
    public Optional<Order> findById(String orderId) {
        return crud.findById(orderId);
    }
}
