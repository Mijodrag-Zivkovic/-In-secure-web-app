package com.News.News.exceptions;

import lombok.Data;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;
import org.springframework.http.HttpStatus;


public class AppException extends RuntimeException {
    private final ErrorCode errorCode;

    public AppException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }


}