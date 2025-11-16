package com.currenjin.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public record UserServiceRoutingFilter(
	boolean userServiceEnabled,
	String monolithUrl,
	String userServiceUrl
) implements GatewayFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String targetUrl = userServiceEnabled ? userServiceUrl : monolithUrl;
		return routeToTarget(exchange, targetUrl);
	}

	// TODO: Not Yet Implemented
	private Mono<Void> routeToTarget(ServerWebExchange exchange, String targetUrl) {
		return null;
	}
}
