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
@Schema(description = "Client entity")
public class Client {
    @Id
    @Column(name = "client_id", nullable = false, length = 14)
    @Schema(description = "Phone number of a Client", type = "string", example = "79034556789")
    private String clientId;

    @Column(name = "password", nullable = false)
    private String password;

    @Transient
    @JsonIgnore
    private String role = "ROLE_USER";

    @Column(name = "tariff_id", nullable = false)
    @Schema(description = "Tariff ID of a Client", type = "string", example = "11")
    private Integer tariffId;

    @Column(name = "balance")
    @Schema(description = "Balance of a Client", type = "number", format = "double", example = "10000.00")
    private BigDecimal balance;
}
