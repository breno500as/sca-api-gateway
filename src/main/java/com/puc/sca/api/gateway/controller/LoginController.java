package com.puc.sca.api.gateway.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.puc.sca.api.gateway.entity.Usuario;
import com.puc.sca.api.gateway.repository.UsuarioRepository;
import com.puc.sca.api.gateway.util.JwtUtil;

@RestController
@RequestMapping("public/login")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class LoginController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Value("${jwt.secret.key}")
	private String secretKey;

	
	@PostMapping
	public @ResponseBody Usuario login(@RequestBody Usuario usuarioPost) {

		final Usuario usuarioAutenticado = this.usuarioRepository.findByEmailAndSenha(usuarioPost.getEmail(),usuarioPost.getSenha());

		if (usuarioAutenticado == null) {
			 throw new BadCredentialsException("usuário não encontrado");
		}

		List<String> permissoes = null;
		
		if (usuarioAutenticado.getPermissoes() != null) {
			permissoes = usuarioAutenticado.getPermissoes().stream().map(permissao -> permissao.getDescricao())
					.collect(Collectors.toList());
			
		}
 		 
		final String token = JwtUtil.buildAuthToken(usuarioAutenticado.getId(), usuarioAutenticado.getNome(), usuarioAutenticado.getEmail(), permissoes);
		usuarioAutenticado.setRoles(permissoes);
		usuarioAutenticado.setToken(token);
		usuarioAutenticado.setSenha(null);
		usuarioAutenticado.setPermissoes(null);

		return usuarioAutenticado;
	}

}
