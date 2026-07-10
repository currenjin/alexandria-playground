package com.wemeet.eventbackbone.oms;

import com.wemeet.eventbackbone.EnableEventBackbone;
import com.wemeet.eventbackbone.common.event.contract.EventTypes;
import com.wemeet.eventbackbone.contracts.ContractCatalog;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * OMS 서비스 (독립 배포). 주문 도메인 + 사가 step(주문확정=진입, 취소=보상)만 — 사가 흐름은 모른다(§7.1.7).
 * @EnableEventBackbone — 백본 공통 빈 활성화(릴레이 스케줄링 포함). 앱은 자기 패키지만 스캔한다.
 */
@SpringBootApplication
@EnableEventBackbone
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
