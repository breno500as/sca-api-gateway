package com.puc.sca.api.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Usuário não autorizado.
 * @author breno
 *
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidTokenJwtException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1582087001823160675L;

	public InvalidTokenJwtException(String exception) {
		super(exception);
	}

	public InvalidTokenJwtException(String exception, Throwable cause) {
		super(exception, cause);
	}

}
