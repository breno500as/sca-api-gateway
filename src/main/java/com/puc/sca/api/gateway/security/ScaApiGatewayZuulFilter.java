package com.puc.sca.api.gateway.security;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.puc.sca.api.gateway.entity.User;
import com.puc.sca.util.pojo.Constants;


/**
 * Filtro responsável por propagar a segurança para os microserviços solicitados pelas aplicações clientes
 * @author breno
 *
 */

public class ScaApiGatewayZuulFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication != null) {
			UsernamePasswordAuthenticationToken u = (UsernamePasswordAuthenticationToken) authentication;
		 
   		 
			   // Passando o usuário e permissões para aplicações hospedadas no datacenter interno
				User usuario = (User) u.getPrincipal();
				Map<String, List<String>> newParameterMap = ctx.getRequestQueryParams();

				if (newParameterMap == null) {
					newParameterMap = new HashMap<String, List<String>>();
				}

				newParameterMap.put(Constants.ID_USUARIO_LOGADO, Arrays.asList(usuario.getId().toString()));
				newParameterMap.put(Constants.NOME_USUARIO_LOGADO, Arrays.asList(usuario.getUsername()));
				newParameterMap.put(Constants.EMAIL_USUARIO_LOGADO, Arrays.asList(usuario.getEmail()));

				if (authentication.getAuthorities() != null) {
					Collection<SimpleGrantedAuthority> permissoes = (Collection<SimpleGrantedAuthority>) authentication.getAuthorities();
					String list = permissoes.stream().map(a -> a.getAuthority()).collect(Collectors.joining(","));
					newParameterMap.put(Constants.PERMISSOES_USUARIO_LOGADO, Arrays.asList(list));
				}

				ctx.setRequestQueryParams(newParameterMap);
		     
		}
		
	
		return null;
	}

	@Override
	public String filterType() {
		return PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
