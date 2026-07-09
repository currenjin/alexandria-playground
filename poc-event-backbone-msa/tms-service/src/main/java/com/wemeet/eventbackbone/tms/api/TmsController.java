package com.wemeet.eventbackbone.tms.api;

import com.wemeet.eventbackbone.tms.application.TmsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * 데모 진입점(TMS) — 배차 관련 액션은 TMS 자기 API로. 각 액션은 도메인 처리 후 사실(tms.trip.*)을 발행하고,
 * orchestrator가 그 사실을 받아 오더 상태 동기화·정산을 코디네이션한다.
 *   POST /demo/orders/{orderId}/dispatch     → 배차 (TripDispatched)
 *   POST /demo/orders/{orderId}/deliver      → 배송완료 (TripDelivered)
 *   POST /demo/orders/{orderId}/cancel-trip  → 배차취소 (TripCancelled)
 */
@RestController
@RequestMapping("/demo")
public class TmsController {

    private final TmsService tms;

    public TmsController(TmsService tms) {
        this.tms = tms;
    }

    @PostMapping("/orders/{orderId}/dispatch")
    public Map<String, Object> dispatch(@PathVariable String orderId) {
        try {
            return Map.of("orderId", orderId, "tripId", tms.dispatch(orderId), "result", "배차 → TripDispatched");
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/orders/{orderId}/deliver")
    public Map<String, Object> deliver(@PathVariable String orderId) {
        try {
            tms.deliver(orderId);
            return Map.of("orderId", orderId, "result", "배송완료 → TripDelivered");
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/orders/{orderId}/cancel-trip")
    public Map<String, Object> cancelTrip(@PathVariable String orderId) {
        try {
            tms.cancelTrip(orderId);
            return Map.of("orderId", orderId, "result", "배차취소 → TripCancelled");
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
