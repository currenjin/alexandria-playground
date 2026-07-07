package com.wemeet.eventbackbone.tms.application;

import com.wemeet.eventbackbone.common.event.EventPublisher;
import com.wemeet.eventbackbone.common.event.HandlerRegistry;
import com.wemeet.eventbackbone.contracts.TripContracts.CancelTrip;
import com.wemeet.eventbackbone.contracts.TripContracts.CreateTrip;
import com.wemeet.eventbackbone.contracts.TripContracts.TripCreationFailed;
import com.wemeet.eventbackbone.contracts.TripContracts.TripDispatched;
import com.wemeet.eventbackbone.tms.domain.Trip;
import com.wemeet.eventbackbone.tms.domain.TripRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * TMS 애플리케이션 서비스. 배차/취소 커맨드 처리(step). 사가 무지.
 * 데모: amount="0"이면 "차량 없음" 업무 실패 → 예외가 아니라 TripCreationFailed 발행(§7.1.7).
 */
@Service
public class TmsService {

    private static final Logger log = LoggerFactory.getLogger(TmsService.class);

    private final TripRepository trips;
    private final EventPublisher events;
    private final HandlerRegistry registry;

    public TmsService(TripRepository trips, EventPublisher events, HandlerRegistry registry) {
        this.trips = trips;
        this.events = events;
        this.registry = registry;
    }

    @PostConstruct
    void register() {
        registry.register("tms", CreateTrip.class, this::onCreateTrip);
        registry.register("tms", CancelTrip.class, this::onCancelTrip);
    }

    void onCreateTrip(CreateTrip cmd) {
        if ("0".equals(cmd.amount())) {
            events.publish(new TripCreationFailed(cmd.orderId(), "NO_VEHICLE"));
            log.info("배차 불가 {} -> creation_failed", cmd.orderId());
            return;
        }
        String tripId = "TRIP-" + UUID.randomUUID().toString().substring(0, 8);
        trips.save(new Trip(tripId, cmd.orderId(), "CARR-1", Trip.DISPATCHED));
        events.publish(new TripDispatched(tripId, cmd.orderId(), "CARR-1"));
        log.info("배차 완료 {} -> {}", cmd.orderId(), tripId);
    }

    void onCancelTrip(CancelTrip cmd) {   // 보상
        trips.updateStatus(cmd.tripId(), Trip.CANCELLED);
        log.info("배차 취소(보상) {}", cmd.tripId());
    }
}
