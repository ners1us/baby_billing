package com.brt.exceptions;

public class NotFoundClientException extends RuntimeException {

    public NotFoundClientException() {
    }

    public NotFoundClientException(String message) {
        super(message);
    }

    public NotFoundClientException(Throwable cause) {
        super(cause);
    }

    public NotFoundClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
