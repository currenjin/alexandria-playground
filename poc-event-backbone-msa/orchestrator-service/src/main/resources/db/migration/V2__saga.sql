-- §7.1.7 saga_instance — 중앙 orchestrator(플랫폼 오너 소유) 서비스 DB에만.
-- (V1__outbox_inbox.sql은 platform-core가 제공 — 이 서비스도 이벤트 소비/커맨드 발행에 outbox·inbox 사용)
CREATE TABLE saga_instance (
    saga_id        UUID PRIMARY KEY,
    saga_type      TEXT NOT NULL,
    aggregate_id   TEXT NOT NULL,
    correlation_id TEXT NOT NULL,
    current_step   TEXT,
    status         TEXT NOT NULL,          -- STARTED/DISPATCHED/COMPLETED/COMPENSATING/COMPENSATED
    state          JSONB NOT NULL DEFAULT '{}'::jsonb,
    tenant_id      TEXT,
    timeout_at     TIMESTAMPTZ,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_saga_active ON saga_instance (timeout_at) WHERE status IN ('STARTED','DISPATCHED','COMPENSATING');
CREATE INDEX idx_saga_corr ON saga_instance (correlation_id);
CREATE INDEX idx_saga_aggregate ON saga_instance (aggregate_id);
