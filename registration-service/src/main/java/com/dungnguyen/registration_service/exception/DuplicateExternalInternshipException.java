package com.dungnguyen.registration_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateExternalInternshipException extends RuntimeException {
    public DuplicateExternalInternshipException(String message) {
        super(message);
    }
}