package com.wemeet.eventbackbone.oms.api;

import com.wemeet.eventbackbone.oms.application.OmsService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 데모 진입점(OMS). 오더 생성만 여기서 — 이후 배차/배송/취소 액션은 orchestrator(:8083)가 코디네이션한다.
 *   POST /demo/orders            → 오더 생성(OrderCreated → 프로세스 시작)
 *   GET  /demo/state/{orderId}   → 이 서비스가 아는 오더 상태(사가 상태는 orchestrator :8083)
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
    public Map<String, String> createOrder(@RequestParam(defaultValue = "SHIPPER-1") String shipperId,
                                           @RequestParam(defaultValue = "서울") String origin,
                                           @RequestParam(defaultValue = "부산") String destination,
                                           @RequestParam(defaultValue = "1250000") String amount,
                                           @RequestParam(defaultValue = "KRW") String currency) {
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        oms.create(orderId, shipperId, origin, destination, amount, currency);
        // 프로세스는 orderId(업무 키)로 추적하므로 응답에 correlationId(요청별 추적값)를 실을 필요가 없다.
        return Map.of("orderId", orderId,
                "hint", "배차: POST :8081/demo/dispatches?orderId=" + orderId);
    }

    @PostMapping("/orders/{orderId}/cancel")
    public Map<String, Object> cancel(@PathVariable String orderId,
                                      @RequestParam(defaultValue = "고객 취소") String reason) {
        oms.cancelOrder(orderId, reason);
        return Map.of("orderId", orderId,
                "result", "취소 요청 처리 — 결과는 OrderCancelled 또는 OrderCancelRejected(배차되면 거부)");
    }

    @GetMapping("/state/{orderId}")
    public Map<String, Object> state(@PathVariable String orderId) {
        List<Map<String, Object>> order = jdbc.queryForList("SELECT status FROM orders WHERE order_id=?", orderId);
        return Map.of(
                "orderId", orderId,
                "order", order.isEmpty() ? Map.of() : order.get(0),
                "hint", "사가는 무상태(상관 키=orderId) — 진행은 orchestrator 로그에서 correlationId로 추적");
    }
}
