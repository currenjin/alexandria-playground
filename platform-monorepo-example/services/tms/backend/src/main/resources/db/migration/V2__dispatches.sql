-- TMS 도메인 테이블 (TMS 서비스 DB) — 배차(오더당 여러 번 가능: 취소 후 재배차)
CREATE TABLE dispatches (
    dispatch_id      VARCHAR(64) PRIMARY KEY,
    order_id     VARCHAR(64) NOT NULL,
    carrier_id   VARCHAR(64),
    status       VARCHAR(32) NOT NULL,   -- DISPATCHED / DELIVERED / CANCELLED
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_dispatches_order ON dispatches (order_id);
