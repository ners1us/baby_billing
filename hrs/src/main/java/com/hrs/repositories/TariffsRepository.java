package com.hrs.repositories;

import com.hrs.entities.Tariffs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TariffsRepository extends JpaRepository<Tariffs, Long> {

    Tariffs findByTariffId(Integer tariffId);
}
