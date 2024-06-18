package com.brt.repositories;

import com.brt.entities.BrtHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BrtHistoryRepository extends JpaRepository<BrtHistory, Long> {

    BrtHistory findByClientAndCallerIdAndStartTimeAndEndTime(String client, String callerId, LocalDateTime startTime, LocalDateTime endTime);
}
