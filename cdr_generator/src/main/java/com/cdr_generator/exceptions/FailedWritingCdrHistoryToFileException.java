package com.cdr_generator.exceptions;

public class FailedWritingCdrHistoryToFileException extends RuntimeException {
    public FailedWritingCdrHistoryToFileException() {}

    public FailedWritingCdrHistoryToFileException(String message) {
        super(message);
    }

    public FailedWritingCdrHistoryToFileException(Throwable cause) {
        super(cause);
    }

    public FailedWritingCdrHistoryToFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
