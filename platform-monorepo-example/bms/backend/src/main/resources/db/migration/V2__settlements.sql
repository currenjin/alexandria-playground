-- BMS 도메인 테이블 (BMS 서비스 DB) — 배송 완료된 배차의 운임 정산
CREATE TABLE settlements (
    settlement_id VARCHAR(64) PRIMARY KEY,
    dispatch_id       VARCHAR(64) NOT NULL,
    order_id      VARCHAR(64) NOT NULL,
    amount        VARCHAR(32) NOT NULL,
    status        VARCHAR(32) NOT NULL,  -- COMPLETED
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);
