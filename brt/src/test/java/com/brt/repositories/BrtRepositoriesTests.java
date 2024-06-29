package com.brt.repositories;

import com.brt.entities.BrtHistory;
import com.brt.entities.Client;
import com.brt.entities.TariffPaymentHistory;
import com.brt.environments.BrtEnvironmentTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootTest
public class BrtRepositoriesTests extends BrtEnvironmentTest {

    @Autowired
    private BrtClientRepository brtClientRepository;

    @Autowired
    private BrtHistoryRepository brtHistoryRepository;

    @Autowired
    private TariffPaymentHistoryRepository tariffPaymentHistoryRepository;

    @BeforeEach
    void clearDatabase() {
        brtHistoryRepository.deleteAll();
        tariffPaymentHistoryRepository.deleteAll();
        brtClientRepository.deleteAll();
    }

    @Test
    void saveBrtHistoryToDatabaseTest() {
        // Arrange
        BrtHistory brtHistory = new BrtHistory();
        brtHistory.setType("01");
        brtHistory.setTariffId(11);
        brtHistory.setClient("79021038006");
        brtHistory.setCallerId("79021038008");
        brtHistory.setStartTime(LocalDateTime.now());
        brtHistory.setEndTime(LocalDateTime.now().plusMinutes(5));
        brtHistory.setInternal(true);

        // Act
        brtHistoryRepository.save(brtHistory);

        // Assert
        BrtHistory savedHistory = brtHistoryRepository.findById(brtHistory.getId()).orElse(null);
        Assertions.assertNotNull(savedHistory);
        Assertions.assertEquals(brtHistory.getClient(), savedHistory.getClient());
        Assertions.assertEquals(brtHistory.getCallerId(), savedHistory.getCallerId());
        Assertions.assertEquals(brtHistory.getStartTime().truncatedTo(ChronoUnit.MILLIS), savedHistory.getStartTime().truncatedTo(ChronoUnit.MILLIS));
        Assertions.assertEquals(brtHistory.getEndTime().truncatedTo(ChronoUnit.MILLIS), savedHistory.getEndTime().truncatedTo(ChronoUnit.MILLIS));

    }

    @Test
    void saveTariffPaymentHistoryToDatabaseTest() {
        // Arrange
        TariffPaymentHistory tariffPaymentHistory = new TariffPaymentHistory();
        tariffPaymentHistory.setClientId("79021038006");
        tariffPaymentHistory.setTariffId(12);
        tariffPaymentHistory.setCost(BigDecimal.valueOf(50.00).setScale(2, RoundingMode.HALF_UP));
        tariffPaymentHistory.setTime(LocalDateTime.now());

        // Act
        tariffPaymentHistoryRepository.save(tariffPaymentHistory);

        // Assert
        TariffPaymentHistory savedHistory = tariffPaymentHistoryRepository.findById(tariffPaymentHistory.getId()).orElse(null);
        Assertions.assertNotNull(savedHistory);
        Assertions.assertEquals(tariffPaymentHistory.getClientId(), savedHistory.getClientId());
        Assertions.assertEquals(tariffPaymentHistory.getTariffId(), savedHistory.getTariffId());
        Assertions.assertEquals(tariffPaymentHistory.getTime().truncatedTo(ChronoUnit.MILLIS), savedHistory.getTime().truncatedTo(ChronoUnit.MILLIS));
    }

    @Test
    void findClientByIdTest() {
        // Arrange
        Client client = new Client();
        client.setClientId("79021038006");
        client.setTariffId(12);
        client.setBalance(BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP));
        brtClientRepository.save(client);

        // Act
        Client foundClient = brtClientRepository.findById("79021038006").orElse(null);

        // Assert
        Assertions.assertNotNull(foundClient);
        Assertions.assertEquals(client.getClientId(), foundClient.getClientId());
        Assertions.assertEquals(client.getBalance(), foundClient.getBalance());
        Assertions.assertEquals(client.getTariffId(), foundClient.getTariffId());
    }

    @Test
    void findBrtHistoryByIdTest() {
        // Arrange
        BrtHistory brtHistory = new BrtHistory();
        brtHistory.setTariffId(11);
        brtHistory.setType("01");
        brtHistory.setClient("79021038006");
        brtHistory.setCallerId("79021038008");
        brtHistory.setStartTime(LocalDateTime.now());
        brtHistory.setEndTime(LocalDateTime.now().plusMinutes(5));
        brtHistory.setInternal(true);
        brtHistoryRepository.save(brtHistory);

        // Act
        BrtHistory foundHistory = brtHistoryRepository.findById(brtHistory.getId()).orElse(null);

        // Assert
        Assertions.assertNotNull(foundHistory);
        Assertions.assertEquals(brtHistory.getId(), foundHistory.getId());
        Assertions.assertEquals(brtHistory.getClient(), foundHistory.getClient());
        Assertions.assertEquals(brtHistory.getCallerId(), foundHistory.getCallerId());
        Assertions.assertEquals(brtHistory.getStartTime().truncatedTo(ChronoUnit.MILLIS),
                foundHistory.getStartTime().truncatedTo(ChronoUnit.MILLIS));
        Assertions.assertEquals(brtHistory.getEndTime().truncatedTo(ChronoUnit.MILLIS),
                foundHistory.getEndTime().truncatedTo(ChronoUnit.MILLIS));
    }


    @Test
    void findBrtHistoryByAttributesTest() {
        // Arrange
        BrtHistory brtHistory = new BrtHistory();
        brtHistory.setTariffId(11);
        brtHistory.setType("01");
        brtHistory.setClient("79021038006");
        brtHistory.setCallerId("79021038008");
        brtHistory.setStartTime(LocalDateTime.now());
        brtHistory.setEndTime(LocalDateTime.now().plusMinutes(5));
        brtHistory.setInternal(true);
        brtHistoryRepository.save(brtHistory);

        // Act
        BrtHistory foundHistory = brtHistoryRepository.findByClientAndCallerIdAndStartTimeAndEndTime(
                brtHistory.getClient(), brtHistory.getCallerId(), brtHistory.getStartTime(), brtHistory.getEndTime());

        // Assert
        Assertions.assertNotNull(foundHistory);
        Assertions.assertEquals(brtHistory.getClient(), foundHistory.getClient());
        Assertions.assertEquals(brtHistory.getCallerId(), foundHistory.getCallerId());
        Assertions.assertEquals(brtHistory.getStartTime().truncatedTo(ChronoUnit.MILLIS),
                foundHistory.getStartTime().truncatedTo(ChronoUnit.MILLIS));
        Assertions.assertEquals(brtHistory.getEndTime().truncatedTo(ChronoUnit.MILLIS),
                foundHistory.getEndTime().truncatedTo(ChronoUnit.MILLIS));
    }

    @Test
    void getAllClientsTest() {
        // Arrange
        Client client = new Client();
        client.setClientId("79021038006");
        client.setTariffId(11);
        client.setBalance(BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP));
        brtClientRepository.save(client);

        Client client1 = new Client();
        client1.setClientId("79021038008");
        client1.setTariffId(12);
        client1.setBalance(BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_UP));
        brtClientRepository.save(client1);

        // Act
        List<Client> allClients = brtClientRepository.findAll();

        // Assert
        Assertions.assertNotNull(allClients);
        Assertions.assertEquals(2, allClients.size());
        Assertions.assertTrue(allClients.contains(client));
        Assertions.assertTrue(allClients.contains(client1));
    }

    @Test
    void countClientsTest() {
        // Arrange
        Client client = new Client();
        client.setClientId("79021038006");
        client.setTariffId(11);
        client.setBalance(BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP));
        brtClientRepository.save(client);

        Client client1 = new Client();
        client1.setClientId("79021038008");
        client1.setTariffId(12);
        client1.setBalance(BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_UP));
        brtClientRepository.save(client1);

        // Act
        long clientCount = brtClientRepository.count();

        // Assert
        Assertions.assertEquals(2, clientCount);
    }
}
