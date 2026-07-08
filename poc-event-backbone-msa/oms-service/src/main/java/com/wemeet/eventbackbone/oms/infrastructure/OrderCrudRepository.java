package com.wemeet.eventbackbone.oms.infrastructure;

import com.wemeet.eventbackbone.oms.domain.Order;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/** Spring Data JDBC 리포지토리. INSERT/SELECT는 매핑으로 자동, 상태 변경만 명시 update. */
interface OrderCrudRepository extends CrudRepository<Order, String> {

    @Modifying
    @Query("UPDATE orders SET status = :status, updated_at = now() WHERE order_id = :orderId")
    void updateStatus(@Param("orderId") String orderId, @Param("status") String status);
}
