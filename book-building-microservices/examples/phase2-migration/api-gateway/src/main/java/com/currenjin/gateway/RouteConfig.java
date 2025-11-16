package com.currenjin.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
	@Value("${feature.toggle.user-service-enabled}")
	private boolean userServiceEnabled;

	@Value("${services.monolith.url}")
	private String monolithUrl;

	@Value("${services.user-service.url}")
	private String userServiceUrl;

	public RouteLocator customRoute(RouteLocatorBuilder builder) {
		return builder.routes()
			.route("users", r -> r.path("/api/users/**")
				.filters(f -> f.filter(new UserServiceRoutingFilter(
					userServiceEnabled, monolithUrl, userServiceUrl)))
				.uri("no://op"))
			.route("others", r -> r.path("/**")
				.uri(monolithUrl))
			.build();
	}
}
