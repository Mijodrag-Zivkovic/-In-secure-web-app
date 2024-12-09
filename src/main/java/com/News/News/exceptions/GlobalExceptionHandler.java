package com.News.News.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<String> handleAppException(AppException ex) {
        HttpStatus status;
        switch (ex.getErrorCode()) {
            case NOT_FOUND -> status = HttpStatus.NOT_FOUND;
            case CONFLICT -> status = HttpStatus.CONFLICT;
            default -> status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(status).body(ex.getMessage());
    }


}
