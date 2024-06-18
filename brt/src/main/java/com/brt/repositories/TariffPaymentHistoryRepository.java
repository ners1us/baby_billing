package com.brt.repositories;

import com.brt.entities.TariffPaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TariffPaymentHistoryRepository extends JpaRepository<TariffPaymentHistory, Long> {
}
