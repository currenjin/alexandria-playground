package com.wemeet.eventbackbone.bms.infrastructure;

import com.wemeet.eventbackbone.bms.domain.Settlement;
import org.springframework.data.repository.CrudRepository;

interface SettlementCrudRepository extends CrudRepository<Settlement, String> {
}
