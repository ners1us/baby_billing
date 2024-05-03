package com.baby_billing.brt.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tariff_payments_history")
public class TariffPaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "tariff_id")
    private Integer tariffId;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "time")
    private LocalDateTime time;
}
