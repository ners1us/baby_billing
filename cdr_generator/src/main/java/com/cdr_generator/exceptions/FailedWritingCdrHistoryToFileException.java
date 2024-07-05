package com.cdr_generator.exceptions;

import java.io.IOException;

public class FailedWritingCdrHistoryToFileException extends IOException {
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
