package com.wemeet.eventbackbone.common.context;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * HTTP 진입점 컨텍스트 자동 주입 (§7.1.1) — <b>비즈 개발자는 컨텍스트를 몰라도 된다.</b>
 * 요청마다 tenant·corp·correlationId를 FlowContext에 열고 finally로 닫는다
 * (소비 측 {@code EventConsumerSupport}가 봉투에서 컨텍스트를 여는 것과 대칭 — 양쪽 진입점 모두 공통이 처리).
 *
 * <p>예제는 헤더(X-Tenant-Id 등)/기본값으로 채운다. <b>실제 운영에서는 이 자리에서 JWT를 검증·파싱해
 * 클레임(tenantId·corpId)을 컨텍스트에 넣는다.</b> correlationId는 인바운드 헤더가 있으면 전파, 없으면 진입점이 발급.
 */
public class FlowContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // TODO(실서비스): 인증 필터가 만든 JWT 클레임에서 tenantId·corpId 추출. 예제는 헤더/데모 기본값.
        String tenantId = headerOr(request, "X-Tenant-Id", "dongsuh");
        String corpId = headerOr(request, "X-Corp-Id", "DS-GRP");
        String correlationId = request.getHeader("X-Correlation-Id");   // null이면 openEntry가 발급(§7.1.1 진입점)

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
