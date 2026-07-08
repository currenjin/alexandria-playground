package com.wemeet.eventbackbone.tms.infrastructure;

import com.wemeet.eventbackbone.tms.domain.Trip;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

interface TripCrudRepository extends CrudRepository<Trip, String> {

    @Modifying
    @Query("UPDATE trips SET status = :status, updated_at = now() WHERE trip_id = :tripId")
    void updateStatus(@Param("tripId") String tripId, @Param("status") String status);
}
