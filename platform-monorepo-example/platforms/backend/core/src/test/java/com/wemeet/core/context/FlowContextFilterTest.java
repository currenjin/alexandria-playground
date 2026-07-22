package com.wemeet.core.context;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * HTTP 진입점 필터 — 요청마다 헤더(X-Tenant-Id/X-Corp-Id/X-Correlation-Id)로 FlowContext를 열고 finally로 닫는다.
 * 헤더 없으면 주입된 기본값 사용. 비즈 코드는 컨텍스트를 몰라도 되게 하는 것이 목적.
 */
class FlowContextFilterTest {

    @AfterEach
    void tearDown() {
        FlowContext.clear();
    }

    @Test
    void 헤더에서_tenant_corp_correlation을_읽어_컨텍스트를_연다() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader("X-Tenant-Id")).thenReturn("tenant-h");
        when(req.getHeader("X-Corp-Id")).thenReturn("corp-h");
        when(req.getHeader("X-Correlation-Id")).thenReturn("corr-h");

        AtomicReference<FlowContext.Ctx> seen = new AtomicReference<>();
        FilterChain chain = (r, s) -> seen.set(FlowContext.get());   // 체인 실행 중 컨텍스트가 열려 있어야 한다

        new FlowContextFilter("def-tenant", "def-corp")
                .doFilter(req, mock(HttpServletResponse.class), chain);

        assertThat(seen.get().tenantId()).isEqualTo("tenant-h");
        assertThat(seen.get().corpId()).isEqualTo("corp-h");
        assertThat(seen.get().correlationId()).isEqualTo("corr-h");
    }

    @Test
    void 헤더가_없으면_주입된_기본값을_쓴다() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);   // 모든 헤더 null

        AtomicReference<FlowContext.Ctx> seen = new AtomicReference<>();
        FilterChain chain = (r, s) -> seen.set(FlowContext.get());

        new FlowContextFilter("def-tenant", "def-corp")
                .doFilter(req, mock(HttpServletResponse.class), chain);

        assertThat(seen.get().tenantId()).isEqualTo("def-tenant");
        assertThat(seen.get().corpId()).isEqualTo("def-corp");
        // correlationId는 헤더도 없으니 openEntry가 새로 생성
        assertThat(seen.get().correlationId()).isNotNull();
    }

    @Test
    void 빈_문자열_헤더도_기본값으로_대체된다() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader("X-Tenant-Id")).thenReturn("   ");   // blank → 기본값

        AtomicReference<FlowContext.Ctx> seen = new AtomicReference<>();
        FilterChain chain = (r, s) -> seen.set(FlowContext.get());

        new FlowContextFilter("def-tenant", "def-corp")
                .doFilter(req, mock(HttpServletResponse.class), chain);

        assertThat(seen.get().tenantId()).isEqualTo("def-tenant");
    }

    @Test
    void 체인이_끝나면_컨텍스트를_닫는다() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        FilterChain chain = mock(FilterChain.class);

        new FlowContextFilter("t", "c")
                .doFilter(req, mock(HttpServletResponse.class), chain);

        verify(chain).doFilter(Mockito.any(), Mockito.any());
        assertThat(FlowContext.get()).isNull();   // finally에서 clear
    }

    @Test
    void 체인이_예외를_던져도_컨텍스트를_닫는다() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        FilterChain chain = (r, s) -> { throw new RuntimeException("boom"); };

        assertThatThrownBy(() ->
                new FlowContextFilter("t", "c").doFilter(req, mock(HttpServletResponse.class), chain))
                .isInstanceOf(RuntimeException.class);

        assertThat(FlowContext.get()).isNull();   // 예외에도 finally clear
    }
}
