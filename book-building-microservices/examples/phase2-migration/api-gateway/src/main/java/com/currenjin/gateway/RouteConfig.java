package com.currenjin.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
	@Value("${feature.toggle.user-service-enabled}")
	private boolean userServiceEnabled;

	@Value("${services.monolith.url}")
	private String monolithUrl;

	@Value("${services.user-service.url}")
	private String userServiceUrl;

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		String userApiTarget = userServiceEnabled ? userServiceUrl : monolithUrl;

		return builder.routes()
				.route("user-api", route -> route.path("/api/users/**")
						.filters(f -> f.addRequestHeader("X-Routed-By", "API-Gateway"))
						.uri(userApiTarget))
				.route("others", route -> route.path("/**")
						.uri(monolithUrl))
				.build();
	}
}
