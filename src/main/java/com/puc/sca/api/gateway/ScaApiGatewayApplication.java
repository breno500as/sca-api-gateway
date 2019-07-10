package com.puc.sca.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.puc.sca.api.gateway.filter.AuthorizationHeaderFilter;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class ScaApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScaApiGatewayApplication.class, args);
	}

	@Bean
	public AuthorizationHeaderFilter authHeaderFilter() {
		return new AuthorizationHeaderFilter();
	}

}
