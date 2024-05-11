package com.baby_billing.hrs.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность для хранения информации о трафике (звонках).
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "traffic")
public class Traffic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false, length = 14)
    private String clientId;

    @Column(name = "tariff_id", nullable = false)
    private Integer tariffId;

    @Column(name = "month")
    private Integer month;

    @Column(name = "minutes_int_current_month")
    private Long minutesIntCurrentMonth;

    @Column(name = "minutes_ext_current_month")
    private Long minutesExtCurrentMonth;

    public Traffic (String clientId,
                    Integer tariffId,
                    Integer month) {
        this.clientId = clientId;
        this.tariffId = tariffId;
        this.month = month;
    }
}
