package com.baby_billing.brt.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Сущность, представляющая историю платежей за тарифы.
 */
@Entity
@Data
@Table(name = "tariff_payments_history")
public class TariffPaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false, length = 14)
    private String clientId;

    @Column(name = "tariff_id", nullable = false)
    private Integer tariffId;

    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

}
