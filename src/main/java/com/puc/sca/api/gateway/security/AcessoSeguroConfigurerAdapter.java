package com.puc.sca.api.gateway.security;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.puc.util.pojo.Constants;

/**
 * Configurações do spring security, tais como url protegidas, cors, filtros de segurança, etc.
 * @author breno
 *
 */

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class AcessoSeguroConfigurerAdapter extends WebSecurityConfigurerAdapter {
	
	private static final String PUBLIC_PATH = "/public/**";

	private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(new AntPathRequestMatcher(PUBLIC_PATH));

	private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);

	@Value("${jwt.secret.key}")
	private String secretKey;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		   http
		    .cors()
			.and()
			.csrf()
			.disable()
			.sessionManagement()
		    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		    .and()
		    .exceptionHandling()
			.defaultAuthenticationEntryPointFor(forbiddenEntryPoint(), PROTECTED_URLS)
			.and()
            .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests()
	        .antMatchers(PUBLIC_PATH, "/api-docs/**", "/swagger-ui.html**")
	        .permitAll();
		    new CrudMicroserviceRoles().configureSecurity(http);
		    new MonitorMicroserviceRoles().configureSecurity(http);
	}

	@Bean
	public JwtFilter authFilter() throws Exception {
		final JwtFilter filter = new JwtFilter(PROTECTED_URLS, this.secretKey);
		filter.setAuthenticationManager(authenticationManagerBean());
		return filter;
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public AuthenticationEntryPoint forbiddenEntryPoint() {
		return new HttpStatusEntryPoint(FORBIDDEN);
	}
	
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");  
		config.addAllowedHeader("*");
		config.addExposedHeader(Constants.AUTHORIZATION_HEADER);
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	
	 

}
