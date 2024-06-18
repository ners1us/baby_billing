package com.hrs.models;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Модель для представления превышения лимита.
 */
@Data
public class OverLimit {

    private BigDecimal internalIncoming;

    private BigDecimal internalOutcoming;

    private BigDecimal externalIncoming;

    private BigDecimal externalOutcoming;

    private Integer referenceTariffId;

}