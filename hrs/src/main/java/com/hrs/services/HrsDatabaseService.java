package com.hrs.services;

import com.hrs.dto.BrtHistory;
import com.hrs.entities.Tariffs;

import java.math.BigDecimal;

public interface HrsDatabaseService {

    void populateHrsTariffsData();

    long countTariffs();

    void saveCallData(BrtHistory brtHistory, long duration, BigDecimal cost, int currentMonth);

    Tariffs getTariff(Integer tariffId);

}
