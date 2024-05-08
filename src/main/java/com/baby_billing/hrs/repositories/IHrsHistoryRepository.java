package com.baby_billing.hrs.repositories;

import com.baby_billing.hrs.entities.HrsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IHrsHistoryRepository extends JpaRepository<HrsHistory, Long> {

    List<HrsHistory> findByClientIdAndCallerIdAndStartTimeAndEndTime(String clientId, String callerId, LocalDateTime startTime, LocalDateTime endTime);
}
