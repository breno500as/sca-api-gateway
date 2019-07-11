package com.puc.sca.api.gateway.filter;

import static org.apache.commons.lang3.StringUtils.removeStart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.puc.sca.api.gateway.entity.Usuario;
import com.puc.sca.api.gateway.util.JwtUtil;

/**
 * Filtro responsável por verificar se o web token foi repassado nas requisições privadas.
 * @author breno
 *
 */

public class AcessoSeguroFilter extends AbstractAuthenticationProcessingFilter {

	public static final String BEARER = "Bearer";

 

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
		
		final List<String> dadosUsuario = JwtUtil.getDadosUsuarioToken(token);
	
		final Usuario usuario = new Usuario();
		usuario.setId(Long.parseLong(dadosUsuario.get(0)));
		usuario.setEmail(dadosUsuario.get(1));
			
		Collection<SimpleGrantedAuthority> authorities = null;
	
		if (dadosUsuario.size() > 2) {
			authorities = new ArrayList<SimpleGrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority(dadosUsuario.get(2)));
		}
	
		return  new UsernamePasswordAuthenticationToken(usuario, token, authorities);
		
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