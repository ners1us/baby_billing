package com.baby_billing.hrs.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MonthCost {

    private String clientId;

    private Integer tariffId;

    private BigDecimal cost;

    private LocalDateTime endTime;
}
