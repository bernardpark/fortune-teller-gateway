package io.spring.cloud.samples.fortuneteller.fortunegateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "url")
@RefreshScope
public class RouteConfiguration {

	private String apiURL = "//fortune-api";
	private String uiURL = "//fortune-ui";
	
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("fortune_teller_api", r -> r
						.path("/api/**")
						.filters(f -> f.rewritePath("/api/(?<path>.*)", "/$\\{path}"))
						.uri(apiURL))
				.route("fortune_teller_ui", r -> r
						.path("/**")
						.uri(uiURL))
				.build();
	}

	public String getApiURL() {
		return apiURL;
	}

	public void setApiURL(String apiURL) {
		this.apiURL = apiURL;
	}

	public String getUiURL() {
		return uiURL;
	}

	public void setUiURL(String uiURL) {
		this.uiURL = uiURL;
	}
	
}
