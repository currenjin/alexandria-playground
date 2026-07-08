package com.wemeet.eventbackbone.common.saga;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * 사가 인스턴스 저장 포트 (§7.1.7, <b>공통·중앙 엔진</b>). 구현은 JdbcSagaStore.
 * 재사용 엔진이라 platform-core에 있고, 흐름 정의(SagaDefinition)만 중앙 orchestrator 서비스가 소유한다.
 * 매칭 열쇠 = correlationId.
 */
public interface SagaStore {

    record TimedOut(String correlationId, String aggregateId) {}

    void start(String sagaType, String aggregateId, String correlationId,
               Map<String, Object> state, OffsetDateTime timeoutAt);

    void advance(String correlationId, String status, String currentStep,
                 Map<String, Object> state, OffsetDateTime timeoutAt);

    /** 종착(step/timeout null). */
    void finish(String correlationId, String status);

    Map<String, Object> state(String correlationId);

    List<TimedOut> findTimedOut();
}
