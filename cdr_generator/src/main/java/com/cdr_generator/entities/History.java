package com.cdr_generator.entities;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Сущность, представляющая историю звонков CDR.
 */
@Entity
@Data
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2)
    private String type;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "caller_id", nullable = false)
    private Client caller;

    @Column(name = "start_time", nullable = false)
    private long startTime;

    @Column(name = "end_time", nullable = false)
    private long endTime;

    /**
     * Возвращает строковое представление объекта History в формате:
     * тип, номер_телефона_клиента, номер_телефона_вызываемого, время_начала, время_окончания.
     *
     * @return Строковое представление объекта History.
     */
    @Override
    public String toString() {
        return String.format("%s,%s,%s,%d,%d", type, client.getPhoneNumber(), caller.getPhoneNumber(), startTime, endTime);
    }
}
