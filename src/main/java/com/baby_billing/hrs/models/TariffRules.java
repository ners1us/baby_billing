package com.baby_billing.hrs.models;

import lombok.Data;

/**
 * Модель представления правил тарифа.
 */
@Data
public class TariffRules {

    private String name;

    private String description;

    private String currency;

    private Prepaid prepaid;

    private OverLimit overlimit;

}

