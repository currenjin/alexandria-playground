package com.wemeet.eventbackbone.bms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/** BMS 서비스 (독립 배포). 정산 도메인. @EnableScheduling — 자기 outbox 릴레이. */
@SpringBootApplication(scanBasePackages = "com.wemeet.eventbackbone")
@EnableScheduling
public class BmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(BmsApplication.class, args);
    }
}
