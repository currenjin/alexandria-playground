package com.wemeet.core.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 컨텍스트 필터 등록 — 웹 서비스에서만 (§7.1.1). platform-core는 라이브러리라 web을 강제하지 않으므로
 * {@code @ConditionalOnWebApplication}으로 비-웹 서비스에는 이 설정 자체가 로드되지 않는다
 * (그래서 그쪽엔 spring-web이 없어도 안전). Filter 타입 빈은 Spring Boot가 서블릿 체인에 자동 등록.
 *
 * <p>기본 tenant·corp는 {@code platform.context.default-tenant-id / default-corp-id}로 주입한다.
 * 미설정 시 빈 값(라이브러리는 특정 테넌트를 모른다) — 데모 기본값은 앱 application.yml에서 지정.
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebContextConfig {

    @Bean
    public FlowContextFilter flowContextFilter(
            @Value("${platform.context.default-tenant-id:}") String defaultTenantId,
            @Value("${platform.context.default-corp-id:}") String defaultCorpId) {
        return new FlowContextFilter(defaultTenantId, defaultCorpId);
    }
}
