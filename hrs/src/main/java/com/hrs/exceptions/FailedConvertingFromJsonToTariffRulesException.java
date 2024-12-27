package com.hrs.exceptions;

public class FailedConvertingFromJsonToTariffRulesException extends RuntimeException {

    public FailedConvertingFromJsonToTariffRulesException() {
    }

    public FailedConvertingFromJsonToTariffRulesException(String message) {
        super(message);
    }

    public FailedConvertingFromJsonToTariffRulesException(Throwable cause) {
        super(cause);
    }

    public FailedConvertingFromJsonToTariffRulesException(String message, Throwable cause) {
        super(message, cause);
    }
}
