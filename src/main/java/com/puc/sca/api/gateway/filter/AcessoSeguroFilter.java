package com.puc.sca.api.gateway.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.puc.sca.api.gateway.util.JwtUtil;

/**
 * Filtro reponsável por extrair o token da requisição para requisições
 * privadas.
 * 
 * @author breno
 *
 */

public class AcessoSeguroFilter extends AbstractAuthenticationProcessingFilter {

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
			throw new BadCredentialsException("Missing Authentication Token");
		}

		return JwtUtil.getUsuario(authorizationHeaderToken.split(" ")[1], this.secretKey);

	}

	@Override
	protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain chain, final Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);
		chain.doFilter(request, response);
	}

}
