package com.wemeet.orchestrator.application;

import com.wemeet.core.saga.SagaStore;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * 사가 liveness 감시(TECH-1272 / #31-③). timeout_at이 지난 미완 사가를 스캔해 감지(WARN 로그)한다.
 * 액션 = 알림만(A): 감지만 하고 자동 재시도·보상은 하지 않는다(타임아웃 ≠ 실패 확정).
 */
class SagaTimeoutScannerTest {

    private final SagaStore store = mock(SagaStore.class);
    private final SagaTimeoutScanner scanner = new SagaTimeoutScanner(store);

    @Test
    void 만료된_사가를_스캔해_감지한다_정산다운_시나리오() {
        // 정산 서비스 다운 → AWAIT_SETTLEMENT에서 응답 이벤트 미도착 → 타임아웃
        when(store.findTimedOut()).thenReturn(List.of(
                new SagaStore.TimedOut("ORD-1", "AWAIT_SETTLEMENT", "IN_PROGRESS")));

        scanner.scan();

        verify(store).findTimedOut();
        // 알림만(A) — 자동 보상/재시도 없음: findTimedOut 외 다른 상호작용 없음
        verifyNoMoreInteractions(store);
    }

    @Test
    void 만료가_없으면_아무_조치도_하지_않는다() {
        when(store.findTimedOut()).thenReturn(List.of());

        scanner.scan();

        verify(store).findTimedOut();
        verifyNoMoreInteractions(store);
    }
}
