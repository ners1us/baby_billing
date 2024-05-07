package com.baby_billing.hrs.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "hrs_history")
@Data
public class HrsHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false, length = 14)
    private String clientId;

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

    @Column(name = "duration")
    private Long duration;

    public HrsHistory(String clientId, String callerId, LocalDateTime startTime, LocalDateTime endTime, Integer tariffId, Boolean internal, BigDecimal cost, Long duration) {
        this.clientId = clientId;
        this.callerId = callerId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tariffId = tariffId;
        this.internal = internal;
        this.cost = cost;
        this.duration = duration;
    }

    public HrsHistory(String clientId, String callerId, LocalDateTime startTime, LocalDateTime endTime, Integer tariffId, Boolean internal) {
        this.clientId = clientId;
        this.callerId = callerId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tariffId = tariffId;
        this.internal = internal;
    }

    public HrsHistory() {
    }
}
