package com.baby_billing.hrs.models;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Prepaid implements Serializable {

    private BigDecimal tariffCost;

    private Limits limits;
}