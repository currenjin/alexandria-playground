package com.wemeet.eventbackbone.oms.api;

import com.wemeet.eventbackbone.common.context.FlowContext;
import com.wemeet.eventbackbone.oms.application.OmsService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 데모 진입점. POST /demo/orders 로 주문을 넣고(amount=0이면 배차 불가 → 보상 흐름),
 * GET /demo/state/{orderId} 로 이 서비스가 아는 주문 상태만 조회한다(사가 상태는 orchestrator :8083).
 */
@RestController
@RequestMapping("/demo")
public class OrderController {

    private final OmsService oms;
    private final JdbcTemplate jdbc;

    public OrderController(OmsService oms, JdbcTemplate jdbc) {
        this.oms = oms;
        this.jdbc = jdbc;
    }

    @PostMapping("/orders")
    public Map<String, String> createOrder(@RequestParam(defaultValue = "1250000") String amount,
                                           @RequestParam(defaultValue = "CUST-1") String customerId,
                                           @RequestParam(defaultValue = "KRW") String currency) {
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        oms.confirm(orderId, customerId, amount, currency);
        return Map.of("orderId", orderId,
                "correlationId", FlowContext.get().correlationId(),
                "hint", "0".equals(amount) ? "배차 불가 → 보상 흐름" : "정상 흐름");
    }

    @GetMapping("/state/{orderId}")
    public Map<String, Object> state(@PathVariable String orderId) {
        List<Map<String, Object>> order = jdbc.queryForList("SELECT status FROM orders WHERE order_id=?", orderId);
        return Map.of(
                "orderId", orderId,
                "order", order.isEmpty() ? Map.of() : order.get(0),
                "hint", "사가 상태는 orchestrator :8083/demo/saga/" + orderId);
    }
}
