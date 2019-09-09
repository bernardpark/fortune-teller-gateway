package io.spring.cloud.samples.fortuneteller.fortunegateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PropertyConfiguration.class)
public class RouteConfiguration {

	@Autowired
	PropertyConfiguration propertyConfiguration;
	
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("fortune_teller_api", r -> r
						.path("/api/**")
						.filters(f -> f.rewritePath("/api/(?<path>.*)", "/$\\{path}"))
						.uri(propertyConfiguration.getApiURL()))
				.route("fortune_teller_ui", r -> r
						.path("/**")
						.uri(propertyConfiguration.getUiURL()))
				.build();
	}
	
}
