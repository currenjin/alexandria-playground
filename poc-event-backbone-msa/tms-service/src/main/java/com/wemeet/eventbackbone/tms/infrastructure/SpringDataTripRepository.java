package com.wemeet.eventbackbone.tms.infrastructure;

import com.wemeet.eventbackbone.tms.domain.Trip;
import com.wemeet.eventbackbone.tms.domain.TripRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SpringDataTripRepository implements TripRepository {

    private final TripCrudRepository crud;

    public SpringDataTripRepository(TripCrudRepository crud) {
        this.crud = crud;
    }

    @Override
    public void save(Trip trip) {
        crud.save(trip);
    }

    @Override
    public void updateStatus(String tripId, String status) {
        crud.updateStatus(tripId, status);
    }

    @Override
    public Optional<Trip> findByOrderId(String orderId) {
        return crud.findByOrderId(orderId);
    }
}
