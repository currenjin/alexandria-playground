-- OMS 도메인 테이블 (OMS 서비스 DB)
CREATE TABLE orders (
    order_id     VARCHAR(64) PRIMARY KEY,
    customer_id  VARCHAR(64) NOT NULL,
    amount       VARCHAR(32) NOT NULL,   -- 금액=문자열+currency (§7.1.1)
    currency     VARCHAR(8)  NOT NULL,
    status       VARCHAR(32) NOT NULL,   -- CONFIRMED / CANCELLED
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);
