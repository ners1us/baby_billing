package com.baby_billing.hrs.services;

import com.baby_billing.brt.entities.BrtHistory;
import com.baby_billing.hrs.models.TariffRules;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ICallCostCalculator {

    BigDecimal calculateCallCost(BrtHistory brtHistory, TariffRules tariffRules, long duration);

    long calculateDuration(LocalDateTime startTime, LocalDateTime endTime);
}
