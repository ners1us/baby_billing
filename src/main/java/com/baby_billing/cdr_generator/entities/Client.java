package com.baby_billing.cdr_generator.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", nullable = false, length = 14)
    private String phoneNumber;

    public Client(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Client() {}

}
