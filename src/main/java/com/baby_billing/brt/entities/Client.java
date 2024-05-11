package com.baby_billing.brt.entities;

import jakarta.persistence.*;
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

    public Client(String clientId,
                  Integer tariffId,
                  BigDecimal balance) {
        this.clientId = clientId;
        this.tariffId = tariffId;
        this.balance = balance;
    }

    public Client(String clientId) {
        this.clientId = clientId;
    }
}
