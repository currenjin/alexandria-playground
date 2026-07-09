-- OMS 도메인 테이블 (OMS 서비스 DB) — 루티프로 미들마일 운송 오더
CREATE TABLE orders (
    order_id     VARCHAR(64) PRIMARY KEY,
    shipper_id   VARCHAR(64) NOT NULL,   -- 화주
    origin       VARCHAR(64) NOT NULL,   -- 상차지
    destination  VARCHAR(64) NOT NULL,   -- 하차지
    amount       VARCHAR(32) NOT NULL,   -- 운임=문자열+currency (§7.1.1)
    currency     VARCHAR(8)  NOT NULL,
    status       VARCHAR(32) NOT NULL,   -- CREATED / DISPATCHED / DELIVERED / SETTLED / CANCELLED
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);
