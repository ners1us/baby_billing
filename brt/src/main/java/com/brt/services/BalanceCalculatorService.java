package com.brt.services;

import com.brt.exceptions.NotFoundClientException;

import java.math.BigDecimal;

public interface BalanceCalculatorService {

    void calculateClientBalance(String clientId, BigDecimal cost) throws NotFoundClientException;
}
