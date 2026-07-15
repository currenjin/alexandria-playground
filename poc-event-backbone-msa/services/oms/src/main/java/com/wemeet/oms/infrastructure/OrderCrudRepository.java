package com.wemeet.oms.infrastructure;

import com.wemeet.oms.domain.Order;
import org.springframework.data.repository.CrudRepository;

/** Spring Data JDBC. save()가 @Version으로 INSERT(신규)/낙관적 UPDATE(기존)를 판단. findById 상속. */
interface OrderCrudRepository extends CrudRepository<Order, String> {
}
