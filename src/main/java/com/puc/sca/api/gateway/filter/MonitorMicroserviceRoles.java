package com.puc.sca.api.gateway.filter;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public class MonitorMicroserviceRoles {
	
	public void configureSecurity(HttpSecurity http) throws Exception {
		http.authorizeRequests() 
		.antMatchers("/monitor/dados-sensor").hasRole("ADMIN");
	} 

}
