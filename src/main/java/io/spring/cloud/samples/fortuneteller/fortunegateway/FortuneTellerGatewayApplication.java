package io.spring.cloud.samples.fortuneteller.fortunegateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FortuneTellerGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(FortuneTellerGatewayApplication.class, args);
	}

}
