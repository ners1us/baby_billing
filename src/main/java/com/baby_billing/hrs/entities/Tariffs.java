package com.baby_billing.hrs.entities;

import com.baby_billing.hrs.converters.TariffRulesConverter;
import com.baby_billing.hrs.models.TariffRules;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;

/**
 * Сущность для хранения информации о тарифах.
 */
@Entity
@Table(name = "tariffs")
@Data
public class Tariffs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tariff_id", nullable = false)
    private Integer tariffId;

    @Column(name = "tariff_rules", columnDefinition = "jsonb", nullable = false)
    @Convert(converter = TariffRulesConverter.class)
    @ColumnTransformer(write = "?::jsonb")
    private TariffRules tariffRules;
}
