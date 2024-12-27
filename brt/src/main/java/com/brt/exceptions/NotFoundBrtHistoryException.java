package com.brt.exceptions;

public class NotFoundBrtHistoryException extends RuntimeException {

    public NotFoundBrtHistoryException() {
    }

    public NotFoundBrtHistoryException(String message) {
        super(message);
    }

    public NotFoundBrtHistoryException(Throwable cause) {
        super(cause);
    }

    public NotFoundBrtHistoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
