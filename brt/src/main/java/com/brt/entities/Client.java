package com.brt.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Column(name = "password", nullable = false)
    private String password;

    @Transient
    @JsonIgnore
    private String role = "ROLE_USER";

    @Column(name = "tariff_id", nullable = false)
    private Integer tariffId;

    @Column(name = "balance")
    private BigDecimal balance;
}
