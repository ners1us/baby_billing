package com.cdr_generator.exceptions;

public class FailedOpeningCdrFileException extends RuntimeException {
    public FailedOpeningCdrFileException() {}

    public FailedOpeningCdrFileException(String message) {
        super(message);
    }

    public FailedOpeningCdrFileException(Throwable cause) {
        super(cause);
    }

    public FailedOpeningCdrFileException(String message, Throwable cause) {
        super(message, cause);
    }
}