-- OMS 도메인 테이블 (OMS 서비스 DB) — 오더 생애주기의 단일 권위 + 낙관적 잠금(version)
CREATE TABLE orders (
    order_id     VARCHAR(64) PRIMARY KEY,
    shipper_id   VARCHAR(64) NOT NULL,   -- 화주
    origin       VARCHAR(64) NOT NULL,   -- 상차지
    destination  VARCHAR(64) NOT NULL,   -- 하차지
    amount       VARCHAR(32) NOT NULL,   -- 운임=문자열+currency (§7.1.1)
    currency     VARCHAR(8)  NOT NULL,
    status       VARCHAR(32) NOT NULL,   -- CREATED / DISPATCHED / DELIVERED / SETTLED / CANCELLED
    version      BIGINT      NOT NULL DEFAULT 0,   -- @Version 낙관적 잠금(동시 전이 직렬화)
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);
