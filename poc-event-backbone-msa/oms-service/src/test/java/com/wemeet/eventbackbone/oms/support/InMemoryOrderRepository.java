package com.wemeet.eventbackbone.oms.support;

import com.wemeet.eventbackbone.oms.domain.Order;
import com.wemeet.eventbackbone.oms.domain.OrderRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** 인메모리 리포지토리 더블 (§7.1.8 1층). */
public class InMemoryOrderRepository implements OrderRepository {

    public final Map<String, Order> store = new HashMap<>();

    @Override
    public void save(Order order) {
        store.put(order.orderId(), order);
    }

    @Override
    public void updateStatus(String orderId, String status) {
        Order o = store.get(orderId);
        if (o != null) {
            store.put(orderId, new Order(o.orderId(), o.shipperId(), o.origin(), o.destination(),
                    o.amount(), o.currency(), status));
        }
    }

    @Override
    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(store.get(orderId));
    }

    public Order find(String orderId) {
        return store.get(orderId);
    }
}
