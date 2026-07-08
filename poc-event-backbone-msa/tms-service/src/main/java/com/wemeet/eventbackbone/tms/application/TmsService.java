package com.wemeet.eventbackbone.tms.application;

import com.wemeet.eventbackbone.common.event.EventHandler;
import com.wemeet.eventbackbone.common.event.EventPublisher;
import com.wemeet.eventbackbone.contracts.TripContracts.CancelTrip;
import com.wemeet.eventbackbone.contracts.TripContracts.CreateTrip;
import com.wemeet.eventbackbone.contracts.TripContracts.TripCreationFailed;
import com.wemeet.eventbackbone.contracts.TripContracts.TripDispatched;
import com.wemeet.eventbackbone.tms.domain.Trip;
import com.wemeet.eventbackbone.tms.domain.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 배차 유스케이스(step). 사가는 모른다. 데모에서 amount="0"이면 배차 실패로 보고 실패 이벤트를 낸다.
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

    @EventHandler
    void onCreateTrip(CreateTrip cmd) {
        if ("0".equals(cmd.amount())) {
            events.publish(new TripCreationFailed(cmd.orderId(), "NO_VEHICLE"));
            log.info("배차 불가 {} -> creation_failed", cmd.orderId());
            return;
        }
        var existing = trips.findByOrderId(cmd.orderId());
        if (existing.isPresent()) {
            Trip t = existing.get();
            if (Trip.DISPATCHED.equals(t.status())) {
                events.publish(new TripDispatched(t.tripId(), cmd.orderId(), t.carrierId()));
                log.info("이미 배차됨(멱등) {} -> {} 재통지", cmd.orderId(), t.tripId());
            } else {
                log.info("기존 배차 비활성({}) — 재통지 생략 {}", t.status(), cmd.orderId());
            }
            return;
        }
        String tripId = "TRIP-" + UUID.randomUUID().toString().substring(0, 8);
        trips.save(new Trip(tripId, cmd.orderId(), "CARR-1", Trip.DISPATCHED));
        events.publish(new TripDispatched(tripId, cmd.orderId(), "CARR-1"));
        log.info("배차 완료 {} -> {}", cmd.orderId(), tripId);
    }

    @EventHandler
    void onCancelTrip(CancelTrip cmd) {
        trips.updateStatus(cmd.tripId(), Trip.CANCELLED);
        log.info("배차 취소(보상) {}", cmd.tripId());
    }
}
