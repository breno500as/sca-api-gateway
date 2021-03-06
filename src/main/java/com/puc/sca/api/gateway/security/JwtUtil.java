package com.puc.sca.api.gateway.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.puc.sca.api.gateway.exception.InvalidTokenJwtException;
import com.puc.sca.util.pojo.Constants;

/**
 * Utilitário para geração do web token.
 * 
 * @author breno
 *
 */

public final class JwtUtil {


	/**
	 * Construtor default.
	 */
	private JwtUtil() {
	}

	/**
	 * Gera o token.
	 * 
	 * @param id
	 * @param nome
	 * @param email
	 * @param permissoes
	 * @param jwtSecretKey
	 * @return
	 */

	public static String buildAuthToken(final Long id, final String nome, final String email, final List<String> permissoes, final String secretKey) {
		try {

			final Calendar expiresAt = Calendar.getInstance();
			expiresAt.add(Calendar.HOUR, 1);
			final Algorithm algorithm = Algorithm.HMAC256(secretKey);

			final Builder jwtTokenBuilder = JWT.create()
					.withClaim(Constants.ID_USUARIO_LOGADO, id)
					.withClaim(Constants.NOME_USUARIO_LOGADO, nome)
					.withClaim(Constants.EMAIL_USUARIO_LOGADO, email)
					.withArrayClaim(Constants.PERMISSOES_USUARIO_LOGADO, permissoes != null ? permissoes.stream().toArray(String[]::new) : null)
					.withExpiresAt(expiresAt.getTime());

			return jwtTokenBuilder.sign(algorithm);

		} catch (final IllegalArgumentException e) {
			throw new InvalidTokenJwtException("Erro ao criar o token de autorização");
		} catch (final JWTCreationException e) {
			throw new InvalidTokenJwtException("Erro jwt ao criar o token de autorização");
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
			throw new InvalidTokenJwtException("Erro ao verificar o token de autorização");
		} catch (final TokenExpiredException e) {
			throw new InvalidTokenJwtException("Token expirado");
		} catch (final JWTVerificationException e) {
			throw new InvalidTokenJwtException("Token inválido");
		}
	}

	/**
	 * Recupera e instância um usuário do token.
	 * 
	 * @param authorizationHeaderToken
	 * @param jwtSecretKey
	 * @return
	 */

	public static List<String> getDadosUsuarioToken(final String authorizationHeaderToken, String secretKey) {

		final DecodedJWT jwt = verifyAuthToken(authorizationHeaderToken, secretKey);

		final Claim claimIdUsuario = jwt.getClaim(Constants.ID_USUARIO_LOGADO);

		// Id do usuário é obrigatório no token
		if (claimIdUsuario == null) {
			throw new ResourceNotFoundException("Usuário não encontrado ou inválido.");
		}

		final List<String> dadosUsuario = new ArrayList<String>();

		dadosUsuario.add(claimIdUsuario.asLong().toString());
		dadosUsuario.add(jwt.getClaim(Constants.NOME_USUARIO_LOGADO).asString());
		dadosUsuario.add(jwt.getClaim(Constants.EMAIL_USUARIO_LOGADO).asString());

		final Claim claimPermissoes = jwt.getClaim(Constants.PERMISSOES_USUARIO_LOGADO);

		if (claimPermissoes != null) {
			dadosUsuario.add(Arrays.asList(claimPermissoes.asArray(String.class)).stream().map(s -> s).collect(Collectors.joining(",")));
		}

		return dadosUsuario;
	}

}
