package com.wemeet.eventbackbone.oms.infrastructure;

import com.wemeet.eventbackbone.oms.domain.Order;
import com.wemeet.eventbackbone.oms.domain.OrderRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/** 리포지토리 어댑터 (infrastructure 레이어). */
@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final JdbcTemplate jdbc;

    public JdbcOrderRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @Override
    public void save(Order o) {
        jdbc.update("INSERT INTO orders (order_id, customer_id, amount, currency, status) VALUES (?,?,?,?,?)",
                o.orderId(), o.customerId(), o.amount(), o.currency(), o.status());
    }

    @Override
    public void updateStatus(String orderId, String status) {
        jdbc.update("UPDATE orders SET status=?, updated_at=now() WHERE order_id=?", status, orderId);
    }
}
