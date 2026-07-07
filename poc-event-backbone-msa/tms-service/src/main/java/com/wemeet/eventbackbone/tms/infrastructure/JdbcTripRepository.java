package com.wemeet.eventbackbone.tms.infrastructure;

import com.wemeet.eventbackbone.tms.domain.Trip;
import com.wemeet.eventbackbone.tms.domain.TripRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTripRepository implements TripRepository {

    private final JdbcTemplate jdbc;

    public JdbcTripRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @Override
    public void save(Trip t) {
        jdbc.update("INSERT INTO trips (trip_id, order_id, carrier_id, status) VALUES (?,?,?,?)",
                t.tripId(), t.orderId(), t.carrierId(), t.status());
    }

    @Override
    public void updateStatus(String tripId, String status) {
        jdbc.update("UPDATE trips SET status=?, updated_at=now() WHERE trip_id=?", status, tripId);
    }
}
