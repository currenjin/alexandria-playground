package com.wemeet.eventbackbone.oms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * OMS 서비스 (독립 배포). 주문 도메인 + 주문 이행 사가(흐름 주인). 레이어드 아키텍처.
 * scanBasePackages로 platform-core(common.*)의 공통 인프라 빈을 함께 스캔.
 */
@SpringBootApplication(scanBasePackages = "com.wemeet.eventbackbone")
@EnableScheduling
public class OmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(OmsApplication.class, args);
    }
}
