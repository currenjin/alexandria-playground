-- TMS 도메인 테이블 (TMS 서비스 DB)
CREATE TABLE trips (
    trip_id      VARCHAR(64) PRIMARY KEY,
    order_id     VARCHAR(64) NOT NULL,
    carrier_id   VARCHAR(64),
    status       VARCHAR(32) NOT NULL,   -- DISPATCHED / CANCELLED
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);
