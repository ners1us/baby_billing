package com.baby_billing.cdr_generator.repositories;

import com.baby_billing.cdr_generator.entities.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHistoryRepository extends JpaRepository<History, Long> {
}
