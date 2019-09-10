package io.spring.cloud.samples.fortuneteller.fortunegateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@ConfigurationProperties(prefix = "url")
@RefreshScope
public class GatewayProperties {

	private String apiURL = "http://fortune-api";
	private String uiURL = "http://fortune-ui";

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
