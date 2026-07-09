package com.wemeet.eventbackbone.tms.domain;

import java.util.Optional;

public interface TripRepository {
    void save(Trip trip);
    void updateStatus(String tripId, String status);
    /** 오더의 활성(DISPATCHED) 배차. 취소 후 재배차를 위해 활성 것만 본다. */
    Optional<Trip> findActiveByOrderId(String orderId);
    Optional<Trip> findById(String tripId);
}
