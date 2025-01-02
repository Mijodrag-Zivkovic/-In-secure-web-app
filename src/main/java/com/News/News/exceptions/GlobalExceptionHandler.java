package com.News.News.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, String>> handleAppException(AppException ex) {
        HttpStatus status;
        switch (ex.getErrorCode()) {
            case NOT_FOUND -> status = HttpStatus.NOT_FOUND;
            case CONFLICT -> status = HttpStatus.CONFLICT;
            case FORBIDDEN -> status = HttpStatus.FORBIDDEN;
            default -> status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return ResponseEntity.status(status).body(response);
    }


}
