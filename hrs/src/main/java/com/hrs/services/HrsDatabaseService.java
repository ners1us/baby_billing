package com.hrs.services;

import com.hrs.dto.BrtHistoryDto;
import com.hrs.entities.Tariffs;

import java.math.BigDecimal;

public interface HrsDatabaseService {

    void saveCallData(BrtHistoryDto brtHistoryDto, long duration, BigDecimal cost, int currentMonth);

    Tariffs getTariff(Integer tariffId);

}
