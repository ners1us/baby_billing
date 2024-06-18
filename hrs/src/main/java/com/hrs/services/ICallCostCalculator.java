package com.hrs.services;

import com.hrs.dto.BrtHistory;
import com.hrs.models.TariffRules;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ICallCostCalculator {

    BigDecimal calculateCallCost(BrtHistory brtHistory, TariffRules tariffRules, long duration);

    long calculateDuration(LocalDateTime startTime, LocalDateTime endTime);
}
