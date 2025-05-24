package com.dungnguyen.registration_service.exception;

public class InternshipApplicationDetailNotFoundException extends RuntimeException {
    public InternshipApplicationDetailNotFoundException(String message) {
        super(message);
    }

    public InternshipApplicationDetailNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}