package com.puc.sca.api.gateway.filter;

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
import com.puc.sca.api.gateway.entity.Usuario;

public class AuthorizationHeaderFilter extends ZuulFilter {

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
		 	Usuario usuario = (Usuario) u.getPrincipal();
  
	
   		    // Uma forma de passar o token via header para um outro microserviço
		    ctx.addZuulRequestHeader("Authorization", AcessoSeguroFilter.BEARER + " " + (String) u.getCredentials());
			ctx.set("idUsuario", usuario.getId());
	
			
			Map<String, List<String>> newParameterMap = new HashMap<String, List<String>>();
			
			// Uma forma de passar o id do usuário e as permissões para um outro microserviço;
			newParameterMap.put("idUsuario", Arrays.asList(usuario.getId().toString()));
			
			Collection<SimpleGrantedAuthority> permissoes = (Collection<SimpleGrantedAuthority>) authentication.getAuthorities();
			if (permissoes != null) {
				String list = permissoes.stream().map(a -> a.getAuthority()).collect(Collectors.joining(","));
				newParameterMap.put("permissoes", Arrays.asList(list));
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
