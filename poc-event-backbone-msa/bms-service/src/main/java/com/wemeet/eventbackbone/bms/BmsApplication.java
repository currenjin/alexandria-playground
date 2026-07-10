package com.wemeet.eventbackbone.bms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.wemeet.eventbackbone.common.event.EventTypes;
import com.wemeet.eventbackbone.contracts.ContractCatalog;
import jakarta.annotation.PostConstruct;

/** BMS 서비스 (독립 배포). 정산 도메인. @EnableScheduling — 자기 outbox 릴레이. */
@SpringBootApplication(scanBasePackages = "com.wemeet.eventbackbone")
@EnableScheduling
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
