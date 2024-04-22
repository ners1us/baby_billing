package com.baby_billing.cdr_generator.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "caller_id", nullable = false)
    private Client caller;

    @Column(nullable = false)
    private long startTime;

    @Column(nullable = false)
    private long endTime;

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%d,%d", type, client.getPhoneNumber(), caller.getPhoneNumber(), startTime, endTime);
    }
}
