package com.brt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * Модель для представления стоимости за месяц.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrsMonthCost {

    private String clientId;

    private Integer tariffId;

    private BigDecimal cost;

    private LocalDateTime endTime;

}
