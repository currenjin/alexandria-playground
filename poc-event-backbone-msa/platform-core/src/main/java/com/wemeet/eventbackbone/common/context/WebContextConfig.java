package com.wemeet.eventbackbone.common.context;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 컨텍스트 필터 등록 — 웹 서비스에서만 (§7.1.1). platform-core는 라이브러리라 web을 강제하지 않으므로
 * {@code @ConditionalOnWebApplication}으로 비-웹 서비스(TMS·BMS)에는 이 설정 자체가 로드되지 않는다
 * (그래서 그쪽엔 spring-web이 없어도 안전). Filter 타입 빈은 Spring Boot가 서블릿 체인에 자동 등록.
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebContextConfig {

    @Bean
    public FlowContextFilter flowContextFilter() {
        return new FlowContextFilter();
    }
}
