package com.wemeet.eventbackbone.oms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.wemeet.eventbackbone.common.event.EventTypes;
import com.wemeet.eventbackbone.contracts.ContractCatalog;
import jakarta.annotation.PostConstruct;

/**
 * OMS 서비스 (독립 배포). 주문 도메인 + 사가 step(주문확정=진입, 취소=보상)만 — 사가 흐름은 모른다(§7.1.7).
 * @EnableScheduling — 자기 outbox 릴레이. scanBasePackages로 platform-core(common.*) 공통 인프라 빈을 함께 스캔.
 */
@SpringBootApplication(scanBasePackages = "com.wemeet.eventbackbone")
@EnableScheduling
public class OmsApplication {

    // 이 앱이 사용하는 이벤트 계약을 기동 시 명시적으로 레지스트리에 등록(type↔class). 예제는 전체 카탈로그.
    @PostConstruct
    void registerContracts() {
        ContractCatalog.ALL.forEach(EventTypes::register);
    }
    public static void main(String[] args) {
        SpringApplication.run(OmsApplication.class, args);
    }
}
