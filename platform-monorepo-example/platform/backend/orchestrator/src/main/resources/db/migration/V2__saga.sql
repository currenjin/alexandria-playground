-- saga_instance — 중앙 orchestrator(플랫폼 오너 소유) 서비스 DB에만.
-- (V1__outbox_inbox.sql은 platform-core가 제공 — 이 서비스도 이벤트 소비/커맨드 발행에 outbox·inbox 사용)
-- 매칭 열쇠 = aggregate_id(orderId). 오더당 프로세스 1개이므로 UNIQUE.
CREATE TABLE saga_instance (
    saga_id        UUID PRIMARY KEY,
    saga_type      TEXT NOT NULL,
    aggregate_id   TEXT NOT NULL UNIQUE,   -- orderId (프로세스 매칭 열쇠)
    correlation_id TEXT NOT NULL,          -- 추적용(요청별로 달라짐)
    current_step   TEXT,
    status         TEXT NOT NULL,          -- CREATED/DISPATCHED/DELIVERED/SETTLED/CANCELLED
    state          JSONB NOT NULL DEFAULT '{}'::jsonb,
    tenant_id      TEXT,
    timeout_at     TIMESTAMPTZ,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_saga_corr ON saga_instance (correlation_id);
