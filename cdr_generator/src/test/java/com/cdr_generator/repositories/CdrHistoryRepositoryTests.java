package com.cdr_generator.repositories;

import com.cdr_generator.environments.CdrEnvironmentTest;
import com.cdr_generator.entities.CdrHistory;
import com.cdr_generator.entities.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CdrHistoryRepositoryTests extends CdrEnvironmentTest {

    @Autowired
    private CdrHistoryRepository cdrHistoryRepository;

    @BeforeEach
    void clearDatabase() {
        cdrHistoryRepository.deleteAll();
    }

    @Test
    void shouldSaveCdrHistoryTest() {
        // Arrange
        Client client1 = new Client();
        client1.setId(1L);
        client1.setPhoneNumber("79074437331");

        Client client2 = new Client();
        client2.setId(2L);
        client2.setPhoneNumber("79025249522");

        CdrHistory cdrHistory = new CdrHistory();
        cdrHistory.setType("01");
        cdrHistory.setClient(client1);
        cdrHistory.setCaller(client2);
        cdrHistory.setStartTime(1680307200L);
        cdrHistory.setEndTime(1680310800L);

        // Act
        CdrHistory savedCdrHistory = cdrHistoryRepository.save(cdrHistory);

        // Assert
        assertThat(savedCdrHistory.getId()).isNotNull();
        assertThat(savedCdrHistory.getType()).isEqualTo("01");
        assertThat(savedCdrHistory.getClient().getPhoneNumber()).isEqualTo("79074437331");
        assertThat(savedCdrHistory.getCaller().getPhoneNumber()).isEqualTo("79025249522");
        assertThat(savedCdrHistory.getStartTime()).isEqualTo(1680307200L);
        assertThat(savedCdrHistory.getEndTime()).isEqualTo(1680310800L);
    }

    @Test
    void shouldReadCdrHistoryTest() {
        // Arrange
        Client client1 = new Client();
        client1.setId(1L);
        client1.setPhoneNumber("79074437331");

        Client client2 = new Client();
        client2.setId(2L);
        client2.setPhoneNumber("79025249522");

        CdrHistory cdrHistory1 = new CdrHistory();
        cdrHistory1.setType("01");
        cdrHistory1.setClient(client1);
        cdrHistory1.setCaller(client2);
        cdrHistory1.setStartTime(1680307200L);
        cdrHistory1.setEndTime(1680310800L);

        CdrHistory cdrHistory2 = new CdrHistory();
        cdrHistory2.setType("02");
        cdrHistory2.setClient(client2);
        cdrHistory2.setCaller(client1);
        cdrHistory2.setStartTime(1680307200L);
        cdrHistory2.setEndTime(1680310800L);

        cdrHistoryRepository.save(cdrHistory1);
        cdrHistoryRepository.save(cdrHistory2);

        // Act
        List<CdrHistory> cdrHistories = cdrHistoryRepository.findAll();

        // Assert
        assertThat(cdrHistories).hasSize(2);
        assertThat(cdrHistories).containsExactlyInAnyOrder(cdrHistory1, cdrHistory2);
    }
}
