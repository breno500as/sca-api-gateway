package com.puc.sca.api.gateway.controller;

import javax.annotation.security.RolesAllowed;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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

	@RolesAllowed({"ROLE_ADMIN"})
	@GetMapping("metodo")
	public Usuario testeMetodoAutenticado(Authentication authentication) {
		Usuario u = new Usuario();
	 
		final Usuario usuario = (Usuario) authentication.getPrincipal();
		 System.out.println(usuario);
		return u;
	}

}
