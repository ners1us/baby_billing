package com.brt.services;

import java.math.BigDecimal;

public interface BalanceCalculatorService {

    void calculateClientBalance(String clientId, BigDecimal cost);
}
