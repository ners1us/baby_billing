package com.baby_billing.brt.repositories;

import com.baby_billing.brt.entities.BrtHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface IBrtHistoryRepository extends JpaRepository<BrtHistory, Long> {

    BrtHistory findByClientAndCallerIdAndStartTimeAndEndTime(String client, String callerId, LocalDateTime startTime, LocalDateTime endTime);
}
