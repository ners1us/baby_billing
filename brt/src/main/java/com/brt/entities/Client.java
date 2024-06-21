package com.brt.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Сущность, представляющая клиентов.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "clients")
public class Client {
    @Id
    @Column(name = "client_id", nullable = false, length = 14)
    private String clientId;

    @Column(name = "tariff_id", nullable = false)
    private Integer tariffId;

    @Column(name = "balance")
    private BigDecimal balance;
}
