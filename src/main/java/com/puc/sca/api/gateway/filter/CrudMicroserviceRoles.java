package com.puc.sca.api.gateway.filter;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public class CrudMicroserviceRoles {
	
	public void configureSecurity(HttpSecurity http) throws Exception {
		http.authorizeRequests() 
		.antMatchers("/crud/insumos/**").hasRole("ADMIN");
	} 

}
