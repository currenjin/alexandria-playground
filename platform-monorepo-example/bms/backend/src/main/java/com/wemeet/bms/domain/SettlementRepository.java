package com.wemeet.bms.domain;

import java.util.Optional;

public interface SettlementRepository {
    void save(Settlement settlement);
    Optional<Settlement> findByDispatchId(String dispatchId);
}
