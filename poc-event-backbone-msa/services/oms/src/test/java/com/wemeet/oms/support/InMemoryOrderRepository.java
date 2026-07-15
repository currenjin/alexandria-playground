package com.wemeet.oms.support;

import com.wemeet.oms.domain.Order;
import com.wemeet.oms.domain.OrderRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** 인메모리 리포지토리 더블 (§7.1.8 1층). save는 version을 흉내로 증가(단위 테스트엔 동시성 없음). */
public class InMemoryOrderRepository implements OrderRepository {

    public final Map<String, Order> store = new HashMap<>();

    @Override
    public void save(Order order) {
        long v = order.version() == null ? 0 : order.version();
        store.put(order.orderId(), new Order(order.orderId(), order.shipperId(), order.origin(),
                order.destination(), order.amount(), order.currency(), order.status(), v + 1));
    }

    @Override
    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(store.get(orderId));
    }

    public Order find(String orderId) {
        return store.get(orderId);
    }
}
