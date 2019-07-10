package com.puc.sca.api.gateway.filter;

import static org.apache.commons.lang3.StringUtils.removeStart;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.puc.sca.api.gateway.util.JwtUtil;

/**
 * Filtro responsável por verificar se o web token foi repassado nas requisições privadas.
 * @author breno
 *
 */

public class AcessoSeguroFilter extends AbstractAuthenticationProcessingFilter {

	public static final String BEARER = "Bearer";

	private String secretKey;

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public AcessoSeguroFilter(final RequestMatcher requiresAuth) {
		super(requiresAuth);

	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		final String authorizationHeaderToken = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);

		if (authorizationHeaderToken == null) {
			throw new BadCredentialsException("Token de autenticação é obrigatório");
		}

		final String token = removeStart(authorizationHeaderToken, BEARER).trim();

		return JwtUtil.getUsuarioAutenticacaoToken(token, this.secretKey);

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

		SecurityContextHolder.getContext().setAuthentication(authResult);

		// Dispara o método da API
		if (this.eventPublisher != null) {
			eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
		}
		
		chain.doFilter(request, response);
	}

}
