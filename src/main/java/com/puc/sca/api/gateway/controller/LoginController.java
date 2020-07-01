package com.puc.sca.api.gateway.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.puc.sca.api.gateway.entity.Usuario;
import com.puc.sca.api.gateway.repository.UsuarioRepository;
import com.puc.sca.api.gateway.security.JwtUtil;

/**
 * Rest controller responsável por gerenciar autenticação e autorização dos microserviços do sistema.
 * @author breno
 *
 */

@RestController
@RequestMapping("public/login")
public class LoginController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Value("${jwt.secret.key}")
	private String secretKey;

	
	@PostMapping
	public @ResponseBody Usuario login(@RequestBody Usuario usuarioPost) {

		final Usuario usuarioAutenticado = this.usuarioRepository.findByEmailAndPassword(usuarioPost.getEmail(),usuarioPost.getPassword());

		if (usuarioAutenticado == null) {
			 throw new BadCredentialsException("usuário não encontrado");
		}

		List<String> permissoes = null;
		
		if (usuarioAutenticado.getAuthorities() != null) {
			permissoes = usuarioAutenticado.getAuthorities().stream().map(permissao -> permissao.getDescription())
					.collect(Collectors.toList());
			
		}
 		 
		final String token = JwtUtil.buildAuthToken(usuarioAutenticado.getId(), usuarioAutenticado.getUsername(), usuarioAutenticado.getEmail(), permissoes, this.secretKey);
		usuarioAutenticado.setToken(token);
		usuarioAutenticado.setPassword(null);
		return usuarioAutenticado;
	}

}
