package com.puc.sca.api.gateway.controller;

import java.util.Arrays;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public ResponseEntity<User> findById(@PathVariable("id") Long id) {
		final User usuario = this.usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(User.NAO_ENCONTRADO));
		return ResponseEntity.ok(usuario);
	}
	
	/**
	 * Regra que permite o update do usuário apenas apenas se o usuário logado (preenchido no token) é o mesmo do usuário a ser updatado. Ou seja garante que o update de usuário
	 * só pode ser feito pelo próprio usuário e não por outros usuários.
	 * @param id
	 * @param user
	 * @return
	 */
	
	@PutMapping("usuarios/{id}")
	@PreAuthorize("@accessManager.isOwner(#id)")
	public ResponseEntity<User> uptade(@PathVariable("id") Long id, @RequestBody @Valid User user) {
		final User usuarioDB =  this.usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(User.NAO_ENCONTRADO));
		BeanUtils.copyProperties(user, usuarioDB);
		return ResponseEntity.ok(this.usuarioRepository.save(usuarioDB));
	}

}
