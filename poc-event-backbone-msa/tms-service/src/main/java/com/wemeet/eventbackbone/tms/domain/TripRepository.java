package com.wemeet.eventbackbone.tms.domain;

import java.util.Optional;

public interface TripRepository {
    void save(Trip trip);
    void updateStatus(String tripId, String status);
    Optional<Trip> findByOrderId(String orderId);
}
