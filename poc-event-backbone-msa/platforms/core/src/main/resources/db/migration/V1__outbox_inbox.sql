-- Outbox · Inbox — 모든 서비스가 자기 DB에 갖는 공통 테이블 (platform-core 제공).
-- MSA: 서비스마다 자기 DB. (모놀리식이면 공통 스키마 모듈별)

CREATE TABLE outbox (
    event_id           UUID PRIMARY KEY,
    seq                BIGSERIAL,
    event_type         TEXT NOT NULL,
    aggregate_id       TEXT NOT NULL,
    tenant_id          TEXT,
    corp_id            TEXT,
    correlation_id     TEXT NOT NULL,
    caused_by_event_id UUID,
    occurred_at        TIMESTAMPTZ NOT NULL,
    version            INT NOT NULL DEFAULT 1,
    payload            JSONB NOT NULL,
    headers            JSONB NOT NULL DEFAULT '{}'::jsonb,
    created_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
    published_at       TIMESTAMPTZ
);
CREATE INDEX idx_outbox_unpublished ON outbox (seq) WHERE published_at IS NULL;

CREATE TABLE inbox (
    consumer_group VARCHAR(64) NOT NULL,
    event_id       UUID NOT NULL,
    processed_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (consumer_group, event_id)
);
