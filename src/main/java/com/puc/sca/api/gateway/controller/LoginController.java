package com.puc.sca.api.gateway.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.puc.sca.api.gateway.entity.Role;
import com.puc.sca.api.gateway.entity.User;
import com.puc.sca.api.gateway.security.JwtUtil;

/**
 * Rest controller responsável por gerenciar autenticação e autorização dos
 * microserviços do sistema.
 * 
 * @author breno
 *
 */

@RestController
@RequestMapping("public/login")
public class LoginController {

	@Value("${jwt.secret.key}")
	private String secretKey;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping
	@ResponseBody
	public  ResponseEntity<User> login(@RequestBody User usuarioPost) {

		try {
			final Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usuarioPost.getUsername(), usuarioPost.getPassword()));
			final User usuarioAutenticado = (User) authentication.getPrincipal();
			
			List<String> permissoes = null;

			if (authentication.getAuthorities() != null) {
				permissoes = usuarioAutenticado.getRoles().stream().map(Role::getName).collect(Collectors.toList());
			}

			final String token = JwtUtil.buildAuthToken(usuarioAutenticado.getId(), usuarioAutenticado.getUsername(), usuarioAutenticado.getEmail(), permissoes, this.secretKey);
			usuarioAutenticado.setToken(token);
			usuarioAutenticado.setPassword(null);

			return ResponseEntity.ok(usuarioAutenticado);

		} catch (AuthenticationException e) {
			  throw new BadCredentialsException("Ocorreu um erro ao efetuar o login do usuário:" + e.getMessage(), e);
		}

	}

}
