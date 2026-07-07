package com.wemeet.eventbackbone.oms.domain.saga;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/** 사가 인스턴스 저장 포트 (domain 레이어). 구현은 infrastructure(JdbcSagaStore). §7.1.7 */
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
