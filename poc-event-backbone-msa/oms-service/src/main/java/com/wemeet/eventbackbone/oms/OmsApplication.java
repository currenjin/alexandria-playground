package com.wemeet.eventbackbone.oms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * OMS 서비스 (독립 배포). 주문 도메인 + 사가 step(주문확정=진입, 취소=보상)만 — 사가 흐름은 모른다(§7.1.7).
 * @EnableScheduling — 자기 outbox 릴레이. scanBasePackages로 platform-core(common.*) 공통 인프라 빈을 함께 스캔.
 */
@SpringBootApplication(scanBasePackages = "com.wemeet.eventbackbone")
@EnableScheduling
public class OmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(OmsApplication.class, args);
    }
}
