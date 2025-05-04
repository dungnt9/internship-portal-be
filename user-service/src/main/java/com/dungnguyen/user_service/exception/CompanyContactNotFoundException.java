package com.dungnguyen.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CompanyContactNotFoundException extends RuntimeException {

    public CompanyContactNotFoundException(String message) {
        super(message);
    }

    public CompanyContactNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}