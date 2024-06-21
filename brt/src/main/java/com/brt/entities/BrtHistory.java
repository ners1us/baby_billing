package com.brt.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Сущность, представляющая историю звонков.
 */
@Entity
@Data
@Table(name = "brt_history")
public class BrtHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false, length = 2)
    private String type;

    @Column(name = "client_id", nullable = false, length = 14)
    private String client;

    @Column(name = "caller_id", nullable = false, length = 14)
    private String callerId;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "tariff_id", nullable = false)
    private Integer tariffId;

    @Column(name = "internal", nullable = false)
    private Boolean internal;

    @Column(name = "cost", columnDefinition = "DECIMAL(38, 2) DEFAULT '0.00'")
    private BigDecimal cost = BigDecimal.ZERO;

}
