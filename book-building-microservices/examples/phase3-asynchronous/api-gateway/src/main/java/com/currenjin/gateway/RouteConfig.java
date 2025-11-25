package com.currenjin.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RouteConfig {

	@Value("${feature.toggle.user-service-enabled}")
	private boolean userServiceEnabled;

	@Value("${feature.toggle.song-service-enabled}")
	private boolean songServiceEnabled;

	@Value("${target.monolith.url}")
	private String monolithUrl;

	@Value("${target.user-service.url}")
	private String userServiceUrl;

	@Value("${target.song-service.url}")
	private String songServiceUrl;

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		String userApiTarget = userServiceEnabled ? userServiceUrl : monolithUrl;
		String songApiTarget = songServiceEnabled ? songServiceUrl : monolithUrl;

		log.info("User API routing to: {} (userServiceEnabled={})",
				userApiTarget, userServiceEnabled);

		return builder.routes()
				.route("user-api", route -> route.path("/api/users/**")
						.filters(f -> f.addRequestHeader("X-Routed-By", "API-Gateway"))
						.uri(userApiTarget))
				.route("song-api", route -> route.path("/api/songs/**")
						.filters(f -> f.addRequestHeader("X-Routed-By", "API-Gateway"))
						.uri(songApiTarget))
				.route("others", route -> route.path("/**")
						.uri(monolithUrl))
				.build();
	}
}