package com.baby_billing.brt.repositories;

import com.baby_billing.brt.entities.BrtHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBrtHistoryRepository extends JpaRepository<BrtHistory, Long> {
}
