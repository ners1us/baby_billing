package com.baby_billing.brt.repositories;

import com.baby_billing.brt.entities.TariffPaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITariffPaymentHistoryRepository extends JpaRepository<TariffPaymentHistory, Long> {
}
