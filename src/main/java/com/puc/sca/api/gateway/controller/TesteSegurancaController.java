package com.puc.sca.api.gateway.controller;

import java.util.Collection;

import javax.annotation.security.RolesAllowed;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.puc.sca.api.gateway.entity.Usuario;

/**
 * POC comprovando autenticação e autorização.
 * 
 * @author breno
 *
 */

@RestController
@RequestMapping("teste")
public class TesteSegurancaController {

	@RolesAllowed("ADMIN")
	@RequestMapping("metodo")
	public Usuario testeMetodoAutenticado(Authentication authentication) {
		Usuario u = new Usuario();
		final Collection<SimpleGrantedAuthority> autoridades = (Collection<SimpleGrantedAuthority>) authentication.getAuthorities();
		final Usuario usuario = (Usuario) authentication.getPrincipal();
		u.setId(1L);
		return u;
	}

}
