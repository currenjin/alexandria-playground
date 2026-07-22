package com.wemeet.orchestrator.application;

import com.wemeet.core.saga.SagaStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사가 liveness 감시 (TECH-1272 / 공통-고민 #31-③). {@code timeout_at}이 지난 미완 사가를 주기 스캔해 감지한다.
 * command를 보냈는데 응답 사실이 안 오면(참여 서비스 다운·유실) 사가가 조용히 멈춘다 — 이 lag를 드러내는 게 목적.
 *
 * <p>액션 = <b>알림만(A)</b>: WARN 로그. 자동 재시도·보상은 하지 않는다(타임아웃 ≠ 실패 확정 — 그냥 지연일 수 있음).
 * orchestrator 서비스에만 둔다(saga_instance 테이블이 이 서비스 DB에만 존재). {@code FOR UPDATE SKIP LOCKED} 조회라 다중 인스턴스 안전.
 */
@Component
public class SagaTimeoutScanner {

    private static final Logger log = LoggerFactory.getLogger(SagaTimeoutScanner.class);

    private final SagaStore saga;

    public SagaTimeoutScanner(SagaStore saga) {
        this.saga = saga;
    }

    @Scheduled(fixedDelayString = "${platform.events.saga.timeout-scan-ms:1000}")
    @Transactional
    public void scan() {
        for (var s : saga.findTimedOut()) {
            log.warn("[saga-stuck] aggregate={} step={} status={} — 사가 타임아웃(응답 이벤트 미도착): 참여 서비스 다운/유실 의심",
                    s.aggregateId(), s.currentStep(), s.status());
        }
    }
}
