package com.roomiohub.backend.shared.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        log.error("Error interno no controlado: ", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor.");
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<ErrorResponse> handleRestException(RestException ex) {
        log.warn("Excepción de Negocio [{}]: {}", ex.getStatus().value(), ex.getMessage());
        return buildErrorResponse(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDenied(AuthorizationDeniedException ex) {
        log.warn("Acceso denegado: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.FORBIDDEN, "Acceso denegado: no tiene permisos para realizar esta acción.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("Error de Validación (400): {}", mensaje);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, mensaje);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Intento de login fallido [Spring Security]: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Credenciales inválidas.");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFound(UsernameNotFoundException ex) {
        log.warn("Intento de login fallido: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "El usuario no existe en el sistema.");
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String mensaje) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .mensaje(mensaje)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }



}
