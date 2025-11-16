package com.currenjin.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public class UserServiceRoutingFilter implements GatewayFilter {
	public UserServiceRoutingFilter(boolean userServiceEnabled, String monolithUrl, String userServiceUrl) {
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		return null;
	}
}
