package com.puc.sca.api.gateway.security;

 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.puc.sca.api.gateway.entity.Role;
import com.puc.sca.api.gateway.entity.User;
import com.puc.sca.api.gateway.service.UserService;
import com.puc.util.pojo.Constants;

/**
 * Filtro responsável por verificar se o web token foi repassado nas requisições privadas.
 * @author breno
 *
 */

public class JwtFilter extends AbstractAuthenticationProcessingFilter {

	public static final String BEARER = "Bearer";
	
	public String secretKey;
	
	@Autowired
	private UserService userService;

	/**
	 * Construtor.
	 * @param requiresAuth - { @link RequestMatcher}
	 * @param secretKey - chave secreta do token jwt { @link JwtUtil}
	 */
 

	public JwtFilter(final RequestMatcher requiresAuth, final String secretKey) {
		super(requiresAuth);
		this.secretKey = secretKey;

	}
	
	/**
	 *  Método responsável por autenticar o usuário validando seu token.
	 */

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		final String authorizationHeaderToken = request.getHeader(Constants.AUTHORIZATION_HEADER);

		if (authorizationHeaderToken == null) {
			throw new BadCredentialsException("Token de autenticação é obrigatório");
		}

		final String token = authorizationHeaderToken.replaceAll(BEARER, "").trim();
		
		final List<String> dadosUsuario = JwtUtil.getDadosUsuarioToken(token, this.secretKey);
		
		final User user = (User) this.userService.loadUserByUsername(dadosUsuario.get(1));
		
		return  new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
		
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
