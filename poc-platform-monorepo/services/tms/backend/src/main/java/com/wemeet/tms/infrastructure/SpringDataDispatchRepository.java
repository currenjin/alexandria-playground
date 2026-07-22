package com.wemeet.tms.infrastructure;

import com.wemeet.tms.domain.Dispatch;
import com.wemeet.tms.domain.DispatchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SpringDataDispatchRepository implements DispatchRepository {

    private final DispatchCrudRepository crud;

    public SpringDataDispatchRepository(DispatchCrudRepository crud) {
        this.crud = crud;
    }

    @Override
    public void save(Dispatch dispatch) {
        crud.save(dispatch);
    }

    @Override
    public void updateStatus(String dispatchId, String status) {
        crud.updateStatus(dispatchId, status);
    }

    @Override
    public Optional<Dispatch> findActiveByOrderId(String orderId) {
        return crud.findActiveByOrderId(orderId);
    }

    @Override
    public Optional<Dispatch> findById(String dispatchId) {
        return crud.findById(dispatchId);
    }
}
