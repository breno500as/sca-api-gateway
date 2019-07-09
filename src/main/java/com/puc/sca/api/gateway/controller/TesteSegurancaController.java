package com.puc.sca.api.gateway.controller;

import javax.annotation.security.RolesAllowed;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.puc.sca.api.gateway.entity.Usuario;

@RestController
@RequestMapping("teste")
public class TesteSegurancaController {

	@RolesAllowed("ADMIN")
	@ResponseBody
	@RequestMapping("metodo")
	public Usuario testeMetodoAutenticado() {
		Usuario u = new Usuario();
		u.setId(1L);
		return u;
	}

}
