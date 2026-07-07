package com.wemeet.eventbackbone.bms.infrastructure;

import com.wemeet.eventbackbone.bms.domain.Settlement;
import com.wemeet.eventbackbone.bms.domain.SettlementRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcSettlementRepository implements SettlementRepository {

    private final JdbcTemplate jdbc;

    public JdbcSettlementRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @Override
    public void save(Settlement s) {
        jdbc.update("INSERT INTO settlements (settlement_id, trip_id, amount, status) VALUES (?,?,?,?)",
                s.settlementId(), s.tripId(), s.amount(), s.status());
    }
}
