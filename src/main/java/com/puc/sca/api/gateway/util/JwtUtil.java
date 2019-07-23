package com.puc.sca.api.gateway.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

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
	 * 
	 */

	public static final String AUTHORIZATION_HEADER = "Authorization";
	
	/**
	 * Apenas para fins didádicos. 
	 * A chave secreta e essa classe podem ser isoladas em uma aplicação com um scheduler diário para atualizar a chave. 
	 * E a comunicação via API com essa aplicação para obter a chave secreta.
	 */
	
	private static final String SECRET_KEY = "e83c7691-515a-4f6c-8048-197b823f0d1b";

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

	public static String buildAuthToken(final Long id, final String email, final List<String> permissoes) {
		try {

			final Calendar expiresAt = Calendar.getInstance();
			expiresAt.add(Calendar.HOUR, 1);
			final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

			final Builder jwtTokenBuilder = JWT.create().withClaim(ID_USUARIO, id).withClaim(EMAIL_USUARIO, email)
					.withArrayClaim(PERMISSOES_USUARIO,
							permissoes != null ? permissoes.stream().toArray(String[]::new) : null)
					.withExpiresAt(expiresAt.getTime());

			return jwtTokenBuilder.sign(algorithm);

		} catch (final IllegalArgumentException e) {
			throw new RuntimeException("Erro ao criar o token de autorização");
		} catch (final JWTCreationException e) {
			throw new RuntimeException("Erro jwt ao criar o token de autorização");
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
			throw new RuntimeException("Erro ao verificar o token de autorização");
		} catch (final TokenExpiredException e) {
			throw new RuntimeException("Token expirado");
		} catch (final JWTVerificationException e) {
			throw new RuntimeException("Token inválido");
		}
	}

	/**
	 * Recupera e instância um usuário do token.
	 * 
	 * @param authorizationHeaderToken
	 * @param jwtSecretKey
	 * @return
	 */

	public static List<String> getDadosUsuarioToken(final String authorizationHeaderToken) {

		final DecodedJWT jwt = verifyAuthToken(authorizationHeaderToken, SECRET_KEY);

		final Claim claimIdUsuario = jwt.getClaim(ID_USUARIO);

		// Id do usuário é obrigatório no token
		if (claimIdUsuario == null) {
			throw new RuntimeException("Usuário não encontrado ou inválido.");
		}

		final List<String> dadosUsuario = new ArrayList<String>();

		dadosUsuario.add(claimIdUsuario.asLong().toString());
		dadosUsuario.add(jwt.getClaim(EMAIL_USUARIO).asString());

		final Claim claimPermissoes = jwt.getClaim(PERMISSOES_USUARIO);

		if (claimPermissoes != null) {
			dadosUsuario.add(Arrays.asList(claimPermissoes.asArray(String.class)).stream().map(s -> s).collect(Collectors.joining(",")));
		}

		return dadosUsuario;
	}

}
