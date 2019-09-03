package io.spring.cloud.samples.fortuneteller.fortunegateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("fortune_teller_ui", r -> r
						.path("/ui/**")
						.filters(f -> f.rewritePath("/ui/(?<path>.*)", "/$\\{path}"))
						.uri("http://bpark-fortune-ui.apps.pcfone.io"))
				.route("fortune_teller_api", r -> r
						.path("/api/**")
						.filters(f -> f.rewritePath("/api/(?<path>.*)", "/$\\{path}"))
						.uri("http://bpark-fortune-api.apps.pcfone.io"))
				.build();
	}
}
