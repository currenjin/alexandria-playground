package com.wemeet.eventbackbone.common.context;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * HTTP 진입점에서 요청마다 tenant·corp·correlationId를 FlowContext에 열고 finally로 닫는다.
 * 비즈 코드는 컨텍스트를 몰라도 된다(소비 측 EventConsumerSupport와 대칭). 실제 운영에선 여기서 JWT 클레임을 파싱한다.
 */
public class FlowContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String tenantId = headerOr(request, "X-Tenant-Id", "dongsuh");
        String corpId = headerOr(request, "X-Corp-Id", "DS-GRP");
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
