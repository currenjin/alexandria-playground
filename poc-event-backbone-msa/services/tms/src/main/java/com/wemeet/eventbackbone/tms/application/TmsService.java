package com.wemeet.eventbackbone.tms.application;

import com.wemeet.eventbackbone.common.event.consume.EventHandler;
import com.wemeet.eventbackbone.common.event.publish.EventPublisher;
import com.wemeet.eventbackbone.contracts.DispatchContracts.CancelDispatch;
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
 * 배차 유스케이스 — TMS 자기 API(배차·배송완료·배차취소)로 행위하고 사실(tms.dispatch.*)을 발행한다.
 * 커맨드는 사가 보상용 {@code CancelDispatch} 하나만 소비(배차확정 사가가 오더 전이 거부를 받았을 때 배차를 되돌림).
 * 배차취소는 사용자 API와 보상 커맨드가 같은 로직을 공용(활성 배차만 취소 — 멱등·보상 안전).
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

    /** 배차 — 활성 배차 없을 때만 생성(취소 후 재배차 지원), 있으면 멱등. dispatchId 반환. */
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

    /** 배송 완료 — 활성(DISPATCHED) 배차만. */
    @Transactional
    public void deliver(String dispatchId) {
        Dispatch d = dispatches.findById(dispatchId)
                .filter(x -> Dispatch.DISPATCHED.equals(x.status()))
                .orElseThrow(() -> new IllegalStateException("배송완료 불가 — 활성 배차 아님: " + dispatchId));
        dispatches.updateStatus(dispatchId, Dispatch.DELIVERED);
        events.publish(new DispatchDelivered(dispatchId, d.orderId()));
        log.info("배송 완료 {} (order={})", dispatchId, d.orderId());
    }

    /** 배차 취소 — 사용자 API(#3)와 사가 보상 공용. 활성 배차만 취소(그 외 무시 — 멱등·보상 안전). */
    @Transactional
    public void cancelDispatch(String dispatchId) {
        Dispatch d = dispatches.findById(dispatchId).orElse(null);
        if (d == null || !Dispatch.DISPATCHED.equals(d.status())) {
            log.info("배차취소 무시 {} — 상태 {}", dispatchId, d == null ? "없음" : d.status());
            return;
        }
        dispatches.updateStatus(dispatchId, Dispatch.CANCELLED);
        events.publish(new DispatchCancelled(dispatchId, d.orderId()));
        log.info("배차 취소 {} (order={})", dispatchId, d.orderId());
    }

    /** 사가 보상 커맨드 — 배차확정 사가가 오더 전이 거부(취소된 오더 등)를 받으면 배차를 되돌린다. */
    @EventHandler
    void onCancelDispatch(CancelDispatch cmd) {
        log.info("보상 수신: 배차취소 {} (order={})", cmd.dispatchId(), cmd.orderId());
        cancelDispatch(cmd.dispatchId());
    }
}
