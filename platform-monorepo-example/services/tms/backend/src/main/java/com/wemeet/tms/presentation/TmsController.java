package com.wemeet.tms.presentation;

import com.wemeet.tms.application.TmsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * 데모 진입점(TMS) — 리소스 = <b>dispatch(배차)</b>. TMS는 자기 리소스만 노출하고(오더는 OMS 소유),
 * 각 액션은 도메인 처리 후 사실(tms.dispatch.*)을 발행한다. orchestrator가 그 사실을 받아 코디네이션.
 *   POST /demo/dispatches?orderId=            → 배차 생성 (DispatchCreated) · dispatchId 반환
 *   POST /demo/dispatches/{dispatchId}/deliver → 배송완료 (DispatchDelivered)
 *   POST /demo/dispatches/{dispatchId}/cancel  → 배차취소 (DispatchCancelled)
 */
@RestController
@RequestMapping("/demo")
public class TmsController {

    private final TmsService tms;

    public TmsController(TmsService tms) {
        this.tms = tms;
    }

    @PostMapping("/dispatches")
    public Map<String, Object> create(@RequestParam String orderId) {
        try {
            return Map.of("orderId", orderId, "dispatchId", tms.dispatch(orderId), "result", "배차 → DispatchCreated");
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/dispatches/{dispatchId}/deliver")
    public Map<String, Object> deliver(@PathVariable String dispatchId) {
        try {
            tms.deliver(dispatchId);
            return Map.of("dispatchId", dispatchId, "result", "배송완료 → DispatchDelivered");
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/dispatches/{dispatchId}/cancel")
    public Map<String, Object> cancel(@PathVariable String dispatchId) {
        try {
            tms.cancelDispatch(dispatchId);
            return Map.of("dispatchId", dispatchId, "result", "배차취소 → DispatchCancelled");
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
