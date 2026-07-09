package com.wemeet.eventbackbone.tms.application;

import com.wemeet.eventbackbone.common.event.EventPublisher;
import com.wemeet.eventbackbone.contracts.DispatchContracts.DispatchCancelled;
import com.wemeet.eventbackbone.contracts.DispatchContracts.DispatchCreated;
import com.wemeet.eventbackbone.contracts.DispatchContracts.DispatchDelivered;
import com.wemeet.eventbackbone.tms.domain.Dispatch;
import com.wemeet.eventbackbone.tms.domain.DispatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 배차 유스케이스 — TMS 자기 API(TmsController, 리소스=dispatch)로 배차·배송완료·배차취소를 수행하고
 * 결과 사실(tms.dispatch.*)을 발행한다. 커맨드 소비는 없다(publish-only). orchestrator가 이 사실을 받아
 * 오더 상태 동기화·정산 트리거를 코디네이션한다. 배차는 orderId로 생성하고, 이후 조작은 dispatchId(자기 식별자)로 한다.
 */
@Service
public class TmsService {

    private static final Logger log = LoggerFactory.getLogger(TmsService.class);

    private final DispatchRepository dispatches;
    private final EventPublisher events;

    public TmsService(DispatchRepository dispatches, EventPublisher events) {
        this.dispatches = dispatches;
        this.events = events;
    }

    /** 배차 생성 — 활성 배차가 없을 때만(취소 후 재배차 지원). 이미 배차됐으면 멱등(그대로 반환). dispatchId 반환. */
    @Transactional
    public String dispatch(String orderId) {
        var active = dispatches.findActiveByOrderId(orderId);
        if (active.isPresent()) {
            log.info("이미 배차됨(멱등) {} -> {}", orderId, active.get().dispatchId());
            return active.get().dispatchId();
        }
        String dispatchId = "DISP-" + UUID.randomUUID().toString().substring(0, 8);
        dispatches.save(new Dispatch(dispatchId, orderId, "CARR-1", Dispatch.DISPATCHED));
        events.publish(new DispatchCreated(dispatchId, orderId, "CARR-1"));
        log.info("배차 완료 {} -> {}", orderId, dispatchId);
        return dispatchId;
    }

    /** 배송 완료 — 그 배차가 DISPATCHED일 때만. */
    @Transactional
    public void deliver(String dispatchId) {
        Dispatch d = activeOrThrow(dispatchId, "배송완료");
        dispatches.updateStatus(dispatchId, Dispatch.DELIVERED);
        events.publish(new DispatchDelivered(dispatchId, d.orderId()));
        log.info("배송 완료 {} (order={})", dispatchId, d.orderId());
    }

    /** 배차 취소 — DISPATCHED일 때만(배송완료된 배차는 취소 불가). */
    @Transactional
    public void cancelDispatch(String dispatchId) {
        Dispatch d = activeOrThrow(dispatchId, "배차취소");
        dispatches.updateStatus(dispatchId, Dispatch.CANCELLED);
        events.publish(new DispatchCancelled(dispatchId, d.orderId()));
        log.info("배차 취소 {} (order={})", dispatchId, d.orderId());
    }

    private Dispatch activeOrThrow(String dispatchId, String action) {
        return dispatches.findById(dispatchId)
                .filter(d -> Dispatch.DISPATCHED.equals(d.status()))
                .orElseThrow(() -> new IllegalStateException(action + " 불가 — 활성(DISPATCHED) 배차 아님: " + dispatchId));
    }
}
