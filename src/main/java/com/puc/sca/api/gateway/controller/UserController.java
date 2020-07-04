package com.puc.sca.api.gateway.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.puc.sca.api.gateway.entity.User;
import com.puc.sca.api.gateway.repository.UserRepository;

@RestController
public class UserController {

	@Autowired
	private UserRepository usuarioRepository;

	@GetMapping("public/usuarios")
	@HystrixCommand(fallbackMethod = "reliableUsers")
	public ResponseEntity<Iterable<User>> recuperaUsuarios() {
		return ResponseEntity.ok(this.usuarioRepository.findAll());
	}

	public Iterable<User> reliableUsers() {
		return Arrays.asList(new User(1L, "BRENO PEREIRA"));
	}

	@GetMapping("usuarios/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<User> findById(@PathVariable(value = "id") Long id) {
		final User usuario = this.usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
		return ResponseEntity.ok(usuario);
	}

}
