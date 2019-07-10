package com.puc.sca.api.gateway.filter;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

import java.util.Collection;

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
			Collection<SimpleGrantedAuthority> autoridades = (Collection<SimpleGrantedAuthority>) authentication.getAuthorities();
			String authorizationHeader = (String) u.getCredentials();
		    ctx.addZuulRequestHeader("Authorization", AcessoSeguroFilter.BEARER + " " + authorizationHeader);
			ctx.set("idUsuario", usuario.getId());

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
