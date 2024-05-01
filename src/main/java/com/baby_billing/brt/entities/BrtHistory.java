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

    @Column(name = "type")
    private String type;

    @Column(name = "client_id")
    private String client;

    @Column(name = "caller_id")
    private String callerId;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "tariff_id")
    private Integer tariffId;

    @Column(name = "internal")
    private Boolean internal;

    @Column(name = "cost")
    private BigDecimal cost;
}
