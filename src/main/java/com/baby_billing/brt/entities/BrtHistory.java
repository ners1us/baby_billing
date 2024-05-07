package com.baby_billing.brt.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "history")
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

    @Column(name = "cost")
    private BigDecimal cost;
}
