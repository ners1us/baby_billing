package com.hrs.repositories;

import com.hrs.entities.HrsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HrsHistoryRepository extends JpaRepository<HrsHistory, Long> {

    List<HrsHistory> findByClientIdAndCallerIdAndStartTimeAndEndTime(String clientId, String callerId, LocalDateTime startTime, LocalDateTime endTime);
}
