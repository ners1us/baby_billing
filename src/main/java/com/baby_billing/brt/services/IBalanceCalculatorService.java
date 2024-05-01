package com.baby_billing.brt.services;

import java.math.BigDecimal;

public interface IBalanceCalculatorService {

    void calculateClientBalance(String clientId, BigDecimal cost);
}
