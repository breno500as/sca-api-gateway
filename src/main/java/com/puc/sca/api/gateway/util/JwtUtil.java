package com.puc.sca.api.gateway.util;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.puc.sca.api.gateway.entity.Permissao;
import com.puc.sca.api.gateway.entity.Usuario;

/**
 * Utilitário para geração do web token.
 * 
 * @author breno
 *
 */

public final class JwtUtil {

	/**
	 * Id do usuário.
	 */
	private static final String ID_USUARIO = "usuario";

	/**
	 * Email do usuário.
	 */
	private static final String EMAIL_USUARIO = "email_usuario";

	/**
	 * Permissões.
	 */

	private static final String PERMISSOES_USUARIO = "permissoes";

	/**
	 * Header.
	 */

	public static final String AUTHORIZATION_HEADER = "Authorization";

	/**
	 * Construtor default.
	 */
	private JwtUtil() {
	}

	/**
	 * Gera o token.
	 * 
	 * @param id
	 * @param permissoes
	 * @param jwtSecretKey
	 * @return
	 */

	public static String buildAuthToken(final Long id, final String email, final List<Permissao> permissoes,
			final String jwtSecretKey) {
		try {

			final Calendar expiresAt = Calendar.getInstance();
			expiresAt.add(Calendar.HOUR, 1);
			final Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey);

			final Builder jwtTokenBuilder = JWT.create().withClaim(ID_USUARIO, id).withClaim(EMAIL_USUARIO, email)
					.withArrayClaim(PERMISSOES_USUARIO, permissoes.stream().map(permissao -> permissao.getDescricao())
							.collect(Collectors.toList()).stream().toArray(String[]::new))
					.withExpiresAt(expiresAt.getTime());

			return jwtTokenBuilder.sign(algorithm);

		} catch (final IllegalArgumentException e) {
			throw new BadCredentialsException("Erro ao criar o token de autorização");
		} catch (final JWTCreationException e) {
			throw new BadCredentialsException("Erro jwt ao criar o token de autorização");
		}
	}

	/**
	 * Verifica o token pela chave secreta.
	 * 
	 * @param authorizationHeaderToken
	 * @param jwtSecretKey
	 * @return
	 */

	private static DecodedJWT verifyAuthToken(final String authorizationHeaderToken, final String jwtSecretKey) {
		try {

			final Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey);
			final JWTVerifier verifier = JWT.require(algorithm).build();

			return verifier.verify(authorizationHeaderToken);

		} catch (final IllegalArgumentException e) {
			throw new BadCredentialsException("Erro ao verificar o token de autorização");
		} catch (final TokenExpiredException e) {
			throw new BadCredentialsException("Token expirado");
		} catch (final JWTVerificationException e) {
			throw new BadCredentialsException("Token inválido");
		}
	}

	/**
	 * Recupera e instância um usuário do token.
	 * 
	 * @param authorizationHeaderToken
	 * @param jwtSecretKey
	 * @return
	 */

	public static UsernamePasswordAuthenticationToken getUsuarioAutenticacaoToken(final String authorizationHeaderToken,
			final String jwtSecretKey) {

		final DecodedJWT jwt = verifyAuthToken(authorizationHeaderToken, jwtSecretKey);
		final Claim claimIdUsuario = jwt.getClaim(ID_USUARIO);
		final Claim claimEmailUsuario = jwt.getClaim(ID_USUARIO);

		// Id do usuário é obrigatório no token
		if (claimIdUsuario == null) {
			throw new BadCredentialsException("Usuário não encontrado ou inválido.");
		}

		final Usuario usuario = new Usuario();
		usuario.setId(claimIdUsuario.asLong());
		usuario.setEmail(claimEmailUsuario.asString());

		final Claim claimPermissoes = jwt.getClaim(PERMISSOES_USUARIO);

		List<SimpleGrantedAuthority> permissoes = null;

		if (claimPermissoes != null) {
			permissoes = claimPermissoes.asList(String.class).stream()
					.map(descricaoPermissao -> new SimpleGrantedAuthority(descricaoPermissao))
					.collect(Collectors.toList());
		}

		return new UsernamePasswordAuthenticationToken(usuario, null, permissoes);
	}

}
