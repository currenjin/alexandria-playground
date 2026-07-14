package com.wemeet.eventbackbone.common.saga;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * 사가/프로세스 인스턴스 저장 포트 (§7.1.7, <b>공통·중앙 엔진</b>). 구현은 JdbcSagaStore.
 *
 * <p>매칭 열쇠 = <b>aggregateId(업무 키, 예: orderId)</b>. 프로세스가 여러 HTTP 액션(배차·배송·취소)으로
 * 나뉘어 진행돼도, 각 액션·이벤트가 지닌 aggregateId로 같은 인스턴스에 매칭된다(correlationId는 요청별로 달라지므로
 * 추적용으로만 저장). 재사용 엔진이라 platform-core에 있고, 흐름 정의는 중앙 orchestrator 서비스가 소유한다.
 */
public interface SagaStore {

    record TimedOut(String aggregateId, String currentStep, String status) {}

    void start(String sagaType, String aggregateId, String correlationId,
               String status, String currentStep, Map<String, Object> state, OffsetDateTime timeoutAt);

    void advance(String aggregateId, String status, String currentStep,
                 Map<String, Object> state, OffsetDateTime timeoutAt);

    /** 종착(step/timeout null). */
    void finish(String aggregateId, String status);

    Map<String, Object> state(String aggregateId);

    /** timeout_at이 설정된 진행 중 인스턴스의 만료분(사용 안 하면 항상 빈 리스트). */
    List<TimedOut> findTimedOut();

    /** 현재 상태(없으면 null). 종료상태 가드·멱등에 쓴다. */
    String status(String aggregateId);
}
