package com.puc.sca.api.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.puc.sca.api.gateway.entity.Usuario;
import com.puc.sca.api.gateway.repository.UsuarioRepository;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping
	public Iterable<Usuario> recuperaUsuarios() {
		return this.usuarioRepository.findAll();
	}

}
