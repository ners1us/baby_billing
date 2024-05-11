package com.baby_billing.hrs.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Модель для представления информации о стоимости звонка.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallCost {

    private String client;

    private String callerId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private BigDecimal cost;

}

