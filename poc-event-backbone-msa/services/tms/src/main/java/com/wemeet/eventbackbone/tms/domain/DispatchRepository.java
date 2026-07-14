package com.wemeet.eventbackbone.tms.domain;

import java.util.Optional;

public interface DispatchRepository {
    void save(Dispatch dispatch);
    void updateStatus(String dispatchId, String status);
    /** 오더의 활성(DISPATCHED) 배차. 취소 후 재배차를 위해 활성 것만 본다. */
    Optional<Dispatch> findActiveByOrderId(String orderId);
    Optional<Dispatch> findById(String dispatchId);
}
