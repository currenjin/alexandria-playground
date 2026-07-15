package com.wemeet.core.context;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * HTTP 진입점에서 요청마다 tenant·corp·correlationId를 FlowContext에 열고 finally로 닫는다.
 * 비즈 코드는 컨텍스트를 몰라도 된다(소비 측 EventConsumerSupport와 대칭). 실제 운영에선 여기서 JWT 클레임을 파싱한다.
 *
 * <p>tenant·corp는 요청 헤더(X-Tenant-Id / X-Corp-Id)에서 읽고, 없으면 주입된 기본값으로 대체한다.
 * 라이브러리는 특정 테넌트를 알지 못하므로 기본값은 빈 값이며, 데모용 기본 테넌트는 사용하는 앱이 설정한다
 * (platform.context.default-tenant-id / default-corp-id).
 */
public class FlowContextFilter extends OncePerRequestFilter {

    private final String defaultTenantId;
    private final String defaultCorpId;

    public FlowContextFilter(String defaultTenantId, String defaultCorpId) {
        this.defaultTenantId = defaultTenantId;
        this.defaultCorpId = defaultCorpId;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String tenantId = headerOr(request, "X-Tenant-Id", defaultTenantId);
        String corpId = headerOr(request, "X-Corp-Id", defaultCorpId);
        String correlationId = request.getHeader("X-Correlation-Id");
        FlowContext.openEntry(tenantId, corpId, correlationId);
        try {
            chain.doFilter(request, response);
        } finally {
            FlowContext.clear();
        }
    }

    private static String headerOr(HttpServletRequest req, String name, String def) {
        String v = req.getHeader(name);
        return (v == null || v.isBlank()) ? def : v;
    }
}
