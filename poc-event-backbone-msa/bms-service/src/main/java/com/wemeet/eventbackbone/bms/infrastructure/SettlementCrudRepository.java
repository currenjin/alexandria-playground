package com.wemeet.eventbackbone.bms.infrastructure;

import com.wemeet.eventbackbone.bms.domain.Settlement;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

interface SettlementCrudRepository extends CrudRepository<Settlement, String> {

    @Query("SELECT * FROM settlements WHERE trip_id = :tripId LIMIT 1")
    Optional<Settlement> findByTripId(@Param("tripId") String tripId);
}
