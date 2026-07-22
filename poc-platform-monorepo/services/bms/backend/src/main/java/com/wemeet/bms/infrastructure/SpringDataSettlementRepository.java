package com.wemeet.bms.infrastructure;

import com.wemeet.bms.domain.Settlement;
import com.wemeet.bms.domain.SettlementRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SpringDataSettlementRepository implements SettlementRepository {

    private final SettlementCrudRepository crud;

    public SpringDataSettlementRepository(SettlementCrudRepository crud) {
        this.crud = crud;
    }

    @Override
    public void save(Settlement settlement) {
        crud.save(settlement);
    }

    @Override
    public Optional<Settlement> findByDispatchId(String dispatchId) {
        return crud.findByDispatchId(dispatchId);
    }
}
