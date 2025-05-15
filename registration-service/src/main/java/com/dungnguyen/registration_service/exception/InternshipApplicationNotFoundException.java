package com.dungnguyen.registration_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InternshipApplicationNotFoundException extends RuntimeException {
    public InternshipApplicationNotFoundException(String message) {
        super(message);
    }
}