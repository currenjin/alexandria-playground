package com.wemeet.eventbackbone.orchestrator.api;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 데모 조회 (api 레이어). 사가 상태는 중앙 orchestrator DB에만 있으므로 여기서만 볼 수 있다(MSA — 각자 자기 DB).
 *   GET /demo/saga/{orderId} → 해당 주문 사가의 status·current_step
 */
@RestController
@RequestMapping("/demo")
public class SagaQueryController {

    private final JdbcTemplate jdbc;

    public SagaQueryController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @GetMapping("/saga/{orderId}")
    public Map<String, Object> saga(@PathVariable String orderId) {
        List<Map<String, Object>> rows = jdbc.queryForList(
                "SELECT status, current_step, correlation_id FROM saga_instance WHERE aggregate_id=?", orderId);
        return Map.of(
                "orderId", orderId,
                "saga", rows.isEmpty() ? Map.of() : rows.get(0));
    }
}
