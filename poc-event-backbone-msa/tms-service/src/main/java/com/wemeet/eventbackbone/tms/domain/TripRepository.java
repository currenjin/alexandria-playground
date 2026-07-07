package com.wemeet.eventbackbone.tms.domain;

public interface TripRepository {
    void save(Trip trip);
    void updateStatus(String tripId, String status);
}
