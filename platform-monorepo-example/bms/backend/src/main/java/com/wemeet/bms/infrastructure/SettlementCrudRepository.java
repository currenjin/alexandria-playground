package com.wemeet.bms.infrastructure;

import com.wemeet.bms.domain.Settlement;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

interface SettlementCrudRepository extends CrudRepository<Settlement, String> {

    @Query("SELECT * FROM settlements WHERE dispatch_id = :dispatchId LIMIT 1")
    Optional<Settlement> findByDispatchId(@Param("dispatchId") String dispatchId);
}
