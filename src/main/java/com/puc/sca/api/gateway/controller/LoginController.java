package com.puc.sca.api.gateway.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.puc.sca.api.gateway.entity.Usuario;
import com.puc.sca.api.gateway.repository.UsuarioRepository;
import com.puc.sca.api.gateway.util.JwtUtil;

@RestController
@RequestMapping("public/login")
public class LoginController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Value("${jwt.secret.key}")
	private String secretKey;

	
	@PostMapping
	public String login(@RequestBody Usuario usuarioPost) {

		final Usuario usuario = this.usuarioRepository.findByEmailAndSenha(usuarioPost.getEmail(),usuarioPost.getSenha());

		if (usuario == null) {
			return HttpStatus.UNAUTHORIZED.toString();
		}

		List<String> permissoes = null;
		
		if (usuario.getPermissoes() != null) {
			permissoes = usuario.getPermissoes().stream().map(permissao -> permissao.getDescricao())
					.collect(Collectors.toList());
		}
 		 
		final String token = JwtUtil.buildAuthToken(usuario.getId(), usuario.getEmail(), permissoes);

		return "{\"token\": \"" +token+ "\"}";
	}

}
