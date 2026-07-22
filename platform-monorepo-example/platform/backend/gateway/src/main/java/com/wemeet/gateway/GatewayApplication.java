package com.wemeet.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API 게이트웨이 — 단일 진입점. 무상태(JWT 공개키 로컬 검증·라우팅만), 세션·인가 판정은 각 서비스.
 * 라우팅은 application.yml의 spring.cloud.gateway.routes로 선언(설정 주입).
 */
@SpringBootApplication
public class GatewayApplication {
  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
  }
}
