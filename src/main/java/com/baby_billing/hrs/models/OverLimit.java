package com.baby_billing.hrs.models;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OverLimit {

    private BigDecimal internalIncoming;

    private BigDecimal internalOutcoming;

    private BigDecimal externalIncoming;

    private BigDecimal externalOutcoming;

    private Integer referenceTariffId;

}