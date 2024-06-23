package com.hrs.services;

import com.hrs.dto.BrtHistoryDto;
import com.hrs.models.TariffRules;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CallCostCalculator {

    BigDecimal calculateCallCost(BrtHistoryDto brtHistoryDto, TariffRules tariffRules, long duration);

    long calculateDuration(LocalDateTime startTime, LocalDateTime endTime);
}
