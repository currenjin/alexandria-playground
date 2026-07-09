package com.wemeet.eventbackbone.tms.infrastructure;

import com.wemeet.eventbackbone.tms.domain.Dispatch;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

interface DispatchCrudRepository extends CrudRepository<Dispatch, String> {

    @Modifying
    @Query("UPDATE dispatches SET status = :status, updated_at = now() WHERE dispatch_id = :dispatchId")
    void updateStatus(@Param("dispatchId") String dispatchId, @Param("status") String status);

    @Query("SELECT * FROM dispatches WHERE order_id = :orderId AND status = 'DISPATCHED' LIMIT 1")
    Optional<Dispatch> findActiveByOrderId(@Param("orderId") String orderId);
}
