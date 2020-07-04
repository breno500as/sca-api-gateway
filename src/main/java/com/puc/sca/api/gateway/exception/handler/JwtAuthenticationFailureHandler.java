package com.puc.sca.api.gateway.exception.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puc.sca.util.exception.ExceptionResponse;

public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private ObjectMapper objectMapper;

	public JwtAuthenticationFailureHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		final PrintWriter out = response.getWriter();

		final ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), exception.getMessage(),
				request.getRequestURI());

		final String jsonError = objectMapper.writeValueAsString(exceptionResponse);

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		response.setContentType("application/json");
		out.print(jsonError);

	}

}
