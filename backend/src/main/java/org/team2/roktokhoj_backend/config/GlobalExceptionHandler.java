package org.team2.roktokhoj_backend.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.team2.roktokhoj_backend.models.ResponseError;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseError> generateResponseError(ResponseStatusException ex) {
        var responseError = new ResponseError();
        responseError.setMessage(ex.getReason());
        responseError.setStatus(ex.getStatusCode().value());
        responseError.setTimeStamp(Instant.now().toEpochMilli());

        return new ResponseEntity<>(responseError, ex.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleValidationException(MethodArgumentNotValidException ex) {
        var responseError = new ResponseError();
        responseError.setMessage(ex.getMessage());
        responseError.setStatus(ex.getStatusCode().value());
        responseError.setTimeStamp(Instant.now().toEpochMilli());

        return new ResponseEntity<>(responseError, ex.getStatusCode());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseError> generateBadCredError(BadCredentialsException ex) {
        var statusCode = HttpStatus.UNAUTHORIZED;
        var responseError = new ResponseError();
        responseError.setMessage("The username or password is incorrect");
        responseError.setStatus(statusCode.ordinal());
        responseError.setTimeStamp(Instant.now().toEpochMilli());

        return new ResponseEntity<>(responseError, statusCode);
    }

    private ResponseEntity<ResponseError> generateForbiddenResponse(String message) {
        var statusCode = HttpStatus.FORBIDDEN;
        var responseError = new ResponseError();
        responseError.setMessage(message);
        responseError.setStatus(statusCode.ordinal());
        responseError.setTimeStamp(Instant.now().toEpochMilli());

        return new ResponseEntity<>(responseError, statusCode);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseError> generateAccessDeniedResponse(Exception ex) {
        return generateForbiddenResponse("You are not authorized to access this resource");
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ResponseError> generateAccessDeniedError(SignatureException ex) {
        return generateForbiddenResponse("The JWT signature is invalid");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ResponseError> generateAccessDeniedError(ExpiredJwtException ex) {
        return generateForbiddenResponse("The JWT token has expired");
    }
}
