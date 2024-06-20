package com.brt.exceptions;

public class NotFoundBrtHistoryException extends Exception {

    public NotFoundBrtHistoryException() {}

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
