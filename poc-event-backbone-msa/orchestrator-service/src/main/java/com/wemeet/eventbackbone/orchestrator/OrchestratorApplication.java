package com.wemeet.eventbackbone.orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Orchestrator 서비스 (독립 배포) — <b>플랫폼 오너 소유 "중앙 sagas"</b> (§7.1.7).
 * 비즈 서비스(OMS/TMS/BMS)가 아니라 공통 플랫폼 팀이 소유하는 배포체.
 * 여기에만 사가 흐름 정의(SagaDefinition)와 사가 상태(saga_instance) DB가 있다.
 * 참여자 서비스는 자기가 어느 사가에 속하는지 모른다 — 커맨드/이벤트만 주고받을 뿐.
 * @EnableScheduling — 자기 outbox 릴레이 + 사가 타임아웃 스캐너.
 */
@SpringBootApplication(scanBasePackages = "com.wemeet.eventbackbone")
@EnableScheduling
public class OrchestratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrchestratorApplication.class, args);
    }
}
