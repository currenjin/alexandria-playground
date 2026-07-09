package com.wemeet.eventbackbone.tms.application;

import com.wemeet.eventbackbone.common.event.EventPublisher;
import com.wemeet.eventbackbone.contracts.TripContracts.TripCancelled;
import com.wemeet.eventbackbone.contracts.TripContracts.TripDelivered;
import com.wemeet.eventbackbone.contracts.TripContracts.TripDispatched;
import com.wemeet.eventbackbone.tms.domain.Trip;
import com.wemeet.eventbackbone.tms.domain.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 배차 유스케이스 — TMS 자기 API(TmsController)로 배차·배송완료·배차취소를 수행하고
 * 결과 사실(tms.trip.*)을 발행한다. 커맨드 소비는 없다(publish-only). orchestrator가 이 사실을 받아
 * 오더 상태 동기화·정산 트리거를 코디네이션한다.
 */
@Service
public class TmsService {

    private static final Logger log = LoggerFactory.getLogger(TmsService.class);

    private final TripRepository trips;
    private final EventPublisher events;

    public TmsService(TripRepository trips, EventPublisher events) {
        this.trips = trips;
        this.events = events;
    }

    /** 배차 — 활성 배차가 없을 때만 새로 생성(취소 후 재배차 지원). 이미 배차됐으면 멱등(그대로 반환). */
    @Transactional
    public String dispatch(String orderId) {
        var active = trips.findActiveByOrderId(orderId);
        if (active.isPresent()) {
            log.info("이미 배차됨(멱등) {} -> {}", orderId, active.get().tripId());
            return active.get().tripId();
        }
        String tripId = "TRIP-" + UUID.randomUUID().toString().substring(0, 8);
        trips.save(new Trip(tripId, orderId, "CARR-1", Trip.DISPATCHED));
        events.publish(new TripDispatched(tripId, orderId, "CARR-1"));
        log.info("배차 완료 {} -> {}", orderId, tripId);
        return tripId;
    }

    /** 배송 완료 — 활성 배차를 완료 처리. */
    @Transactional
    public void deliver(String orderId) {
        Trip t = trips.findActiveByOrderId(orderId)
                .orElseThrow(() -> new IllegalStateException("배송완료 불가 — 활성 배차 없음: " + orderId));
        trips.updateStatus(t.tripId(), Trip.DELIVERED);
        events.publish(new TripDelivered(t.tripId(), orderId));
        log.info("배송 완료 {} (order={})", t.tripId(), orderId);
    }

    /** 배차 취소 — 활성 배차만 취소(배송완료된 배차는 활성 아님 → 거부). */
    @Transactional
    public void cancelTrip(String orderId) {
        Trip t = trips.findActiveByOrderId(orderId)
                .orElseThrow(() -> new IllegalStateException("배차취소 불가 — 활성 배차 없음: " + orderId));
        trips.updateStatus(t.tripId(), Trip.CANCELLED);
        events.publish(new TripCancelled(t.tripId(), orderId));
        log.info("배차 취소 {} (order={})", t.tripId(), orderId);
    }
}
