package org.team2.roktokhoj_backend.config;

import org.springframework.http.ResponseEntity;
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
}
