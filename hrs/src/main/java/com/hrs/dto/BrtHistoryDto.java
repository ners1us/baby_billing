package com.hrs.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BrtHistoryDto {

    private Long id;

    private String type;

    private String client;

    private String callerId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer tariffId;

    private Boolean internal;

    private BigDecimal cost;

}
