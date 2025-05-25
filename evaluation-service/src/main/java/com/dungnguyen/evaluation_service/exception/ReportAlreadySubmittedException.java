package com.dungnguyen.evaluation_service.exception;

public class ReportAlreadySubmittedException extends RuntimeException {
    public ReportAlreadySubmittedException(String message) {
        super(message);
    }
}
