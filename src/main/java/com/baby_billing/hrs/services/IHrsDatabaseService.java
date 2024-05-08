package com.baby_billing.hrs.services;

import com.baby_billing.brt.entities.BrtHistory;
import com.baby_billing.hrs.entities.Tariffs;

import java.math.BigDecimal;

public interface IHrsDatabaseService {

    void populateHrsTariffsData();

    long countTariffs();

    void saveCallData(BrtHistory brtHistory, long duration, BigDecimal cost, int currentMonth);

    Tariffs getTariff(Integer tariffId);

}
