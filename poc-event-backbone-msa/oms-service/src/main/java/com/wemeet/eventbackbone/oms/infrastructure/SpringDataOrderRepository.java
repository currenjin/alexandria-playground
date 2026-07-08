package com.wemeet.eventbackbone.oms.infrastructure;

import com.wemeet.eventbackbone.oms.domain.Order;
import com.wemeet.eventbackbone.oms.domain.OrderRepository;
import org.springframework.stereotype.Repository;

/** 도메인 포트 어댑터 — Spring Data JDBC 리포지토리에 위임(생쿼리 없음). */
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
    public void updateStatus(String orderId, String status) {
        crud.updateStatus(orderId, status);
    }
}
