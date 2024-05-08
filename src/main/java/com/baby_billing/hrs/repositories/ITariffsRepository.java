package com.baby_billing.hrs.repositories;

import com.baby_billing.hrs.entities.Tariffs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITariffsRepository extends JpaRepository<Tariffs, Long> {

    Tariffs findByTariffId(Integer tariffId);
}
