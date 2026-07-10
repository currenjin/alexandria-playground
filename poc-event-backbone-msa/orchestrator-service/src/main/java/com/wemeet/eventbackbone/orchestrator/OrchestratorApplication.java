package com.wemeet.eventbackbone.orchestrator;

import com.wemeet.eventbackbone.EnableEventBackbone;
import com.wemeet.eventbackbone.common.event.contract.EventTypes;
import com.wemeet.eventbackbone.contracts.ContractCatalog;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Orchestrator 서비스 (독립 배포) — <b>플랫폼 오너 소유 "중앙 sagas"</b> (§7.1.7).
 * 비즈 서비스(OMS/TMS/BMS)가 아니라 공통 플랫폼 팀이 소유하는 배포체.
 * 여기에만 사가 흐름 정의(SagaDefinition)와 사가 상태(saga_instance) DB가 있다.
 * 참여자 서비스는 자기가 어느 사가에 속하는지 모른다 — 커맨드/이벤트만 주고받을 뿐.
 * @EnableEventBackbone — 백본 활성화(릴레이 + 사가 타임아웃 스캐너 스케줄링 포함).
 */
@SpringBootApplication
@EnableEventBackbone
public class OrchestratorApplication {

    // 이 앱이 사용하는 이벤트 계약을 기동 시 명시적으로 레지스트리에 등록(type↔class). 예제는 전체 카탈로그.
    @PostConstruct
    void registerContracts() {
        ContractCatalog.ALL.forEach(EventTypes::register);
    }
    public static void main(String[] args) {
        SpringApplication.run(OrchestratorApplication.class, args);
    }
}
