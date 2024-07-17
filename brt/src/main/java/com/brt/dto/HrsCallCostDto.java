package com.brt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrsCallCostDto {

    private String client;

    private String callerId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private BigDecimal cost;

}

