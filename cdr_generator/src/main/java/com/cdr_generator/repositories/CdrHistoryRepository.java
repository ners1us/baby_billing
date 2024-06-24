package com.cdr_generator.repositories;

import com.cdr_generator.entities.CdrHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CdrHistoryRepository extends JpaRepository<CdrHistory, Long> {
}
