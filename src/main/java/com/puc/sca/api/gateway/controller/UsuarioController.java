package com.puc.sca.api.gateway.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.puc.sca.api.gateway.entity.Usuario;
import com.puc.sca.api.gateway.repository.UsuarioRepository;

@RestController
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping("public/usuarios")
	@HystrixCommand(fallbackMethod = "reliableUsers")
	public Iterable<Usuario> recuperaUsuarios() {
		return this.usuarioRepository.findAll();
	}

	public Iterable<Usuario> reliableUsers() {
		return Arrays.asList(new Usuario(1L, "BRENO PEREIRA"));
	}

	@GetMapping("usuarios/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Usuario findById(@PathVariable(value = "id") Long id) {
		return this.usuarioRepository.findById(id).get();
	}

}
