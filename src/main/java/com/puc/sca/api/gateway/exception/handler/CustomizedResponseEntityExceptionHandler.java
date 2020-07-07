package com.puc.sca.api.gateway.exception.handler;


import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.puc.sca.util.exception.ExceptionResponse;



@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
		final ExceptionResponse exceptionResponse = 
				new ExceptionResponse(
                        LocalDateTime.now(),
						request.getDescription(false),
						ex.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(AccessDeniedException.class)
	public final ResponseEntity<ExceptionResponse> handleAccessDeniedExceptions(AccessDeniedException ex, WebRequest request) {
		final ExceptionResponse exceptionResponse = 
				new ExceptionResponse(
						LocalDateTime.now(),
						request.getDescription(false),
						ex.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler({BadCredentialsException.class})
	public final ResponseEntity<ExceptionResponse> handleAuthorizationExceptions(BadCredentialsException ex, WebRequest request) {
		final ExceptionResponse exceptionResponse = 
				new ExceptionResponse(
						LocalDateTime.now(),
						request.getDescription(false),
						ex.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
	}
	
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleResourceBadRequestExceptions(ResourceNotFoundException ex, WebRequest request) {
		final ExceptionResponse exceptionResponse = 
				new ExceptionResponse(
						LocalDateTime.now(),
						request.getDescription(false),
						ex.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}
	
}
