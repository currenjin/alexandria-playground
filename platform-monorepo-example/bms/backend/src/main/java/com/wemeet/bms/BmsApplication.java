package com.wemeet.bms;

import com.wemeet.core.EnableEventBackbone;
import com.wemeet.core.event.contract.EventTypes;
import com.wemeet.contract.ContractCatalog;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** BMS 서비스 (독립 배포). 정산 도메인. @EnableEventBackbone — 백본 활성화(릴레이 스케줄링 포함). */
@SpringBootApplication
@EnableEventBackbone
public class BmsApplication {

    // 이 앱이 사용하는 이벤트 계약을 기동 시 명시적으로 레지스트리에 등록(type↔class). 예제는 전체 카탈로그.
    @PostConstruct
    void registerContracts() {
        ContractCatalog.ALL.forEach(EventTypes::register);
    }
    public static void main(String[] args) {
        SpringApplication.run(BmsApplication.class, args);
    }
}
