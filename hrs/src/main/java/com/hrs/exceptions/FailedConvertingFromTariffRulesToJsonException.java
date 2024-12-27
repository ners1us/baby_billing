package com.hrs.exceptions;

public class FailedConvertingFromTariffRulesToJsonException extends RuntimeException {
    public FailedConvertingFromTariffRulesToJsonException() {
    }

    public FailedConvertingFromTariffRulesToJsonException(String message) {
        super(message);
    }

    public FailedConvertingFromTariffRulesToJsonException(Throwable cause) {
        super(cause);
    }

    public FailedConvertingFromTariffRulesToJsonException(String message, Throwable cause) {
        super(message, cause);
    }
}
