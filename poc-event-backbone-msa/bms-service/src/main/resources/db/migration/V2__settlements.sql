-- BMS 도메인 테이블 (BMS 서비스 DB)
CREATE TABLE settlements (
    settlement_id VARCHAR(64) PRIMARY KEY,
    trip_id       VARCHAR(64) NOT NULL,
    amount        VARCHAR(32) NOT NULL,
    status        VARCHAR(32) NOT NULL,  -- SCHEDULED
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);
