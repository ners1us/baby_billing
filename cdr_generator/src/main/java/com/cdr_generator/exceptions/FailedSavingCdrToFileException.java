package com.cdr_generator.exceptions;

public class FailedSavingCdrToFileException extends RuntimeException {
    public FailedSavingCdrToFileException() {
    }

    public FailedSavingCdrToFileException(String message) {
        super(message);
    }

    public FailedSavingCdrToFileException(Throwable cause) {
        super(cause);
    }

    public FailedSavingCdrToFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
