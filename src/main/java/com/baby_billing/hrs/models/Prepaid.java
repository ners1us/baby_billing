package com.baby_billing.hrs.models;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Модель представления помесячной оплаты.
 */
@Data
public class Prepaid {

    private BigDecimal tariffCost;

    private Limits limits;

}