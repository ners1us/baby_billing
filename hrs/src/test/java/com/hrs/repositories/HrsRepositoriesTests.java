package com.hrs.repositories;

import com.hrs.entities.HrsHistory;
import com.hrs.entities.Traffic;
import com.hrs.environments.HrsEnvironmentTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HrsRepositoriesTests extends HrsEnvironmentTest {

    @Autowired
    private HrsHistoryRepository hrsHistoryRepository;

    @Autowired
    private TrafficRepository trafficRepository;

    @BeforeEach
    void clearDatabase() {
        hrsHistoryRepository.deleteAll();
        trafficRepository.deleteAll();
    }

    @Test
    void findByClientIdTest() {
        // Arrange
        String clientId = "79021038006";
        int tariffId = 11;
        int month = 5;
        Traffic traffic = new Traffic(clientId, tariffId, month);
        trafficRepository.save(traffic);

        // Act
        Traffic foundTraffic = trafficRepository.findByClientId(clientId);

        // Assert
        assertNotNull(foundTraffic);
        assertEquals(clientId, foundTraffic.getClientId());
        assertEquals(tariffId, foundTraffic.getTariffId());
        assertEquals(month, foundTraffic.getMonth());
    }

    @Test
    void findByClientIdAndCallerIdAndStartTimeAndEndTimeTest() {
        // Arrange
        String clientId = "79021038006";
        String callerId = "79021038008";
        LocalDateTime startTime = LocalDateTime.now().minusMinutes(5);
        LocalDateTime endTime = LocalDateTime.now();
        HrsHistory history = new HrsHistory(clientId, callerId, startTime, endTime, 11, true);
        hrsHistoryRepository.save(history);

        // Act
        List<HrsHistory> result = hrsHistoryRepository.findByClientIdAndCallerIdAndStartTimeAndEndTime(clientId, callerId, startTime, endTime);

        // Assert
        assertEquals(1, result.size());
        HrsHistory foundHistory = result.get(0);
        assertEquals(clientId, foundHistory.getClientId());
        assertEquals(callerId, foundHistory.getCallerId());
        assertTrue(foundHistory.getStartTime().truncatedTo(ChronoUnit.MILLIS).isEqual(startTime.truncatedTo(ChronoUnit.MILLIS)));
        assertTrue(foundHistory.getEndTime().truncatedTo(ChronoUnit.MILLIS).isEqual(endTime.truncatedTo(ChronoUnit.MILLIS)));
        assertEquals(11, foundHistory.getTariffId());
        assertTrue(foundHistory.getInternal());
    }
}
