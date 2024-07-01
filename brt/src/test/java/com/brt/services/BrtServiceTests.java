package com.brt.services;

import com.brt.entities.BrtHistory;
import com.brt.entities.Client;
import com.brt.entities.TariffPaymentHistory;
import com.brt.environments.BrtEnvironmentTest;
import com.brt.exceptions.NotFoundClientException;
import com.brt.services.implementations.BrtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Testcontainers
class BrtServiceTests extends BrtEnvironmentTest {

    @Mock
    private BrtDatabaseService brtDatabaseService;

    @Mock
    private BalanceCalculatorService balanceCalculatorService;

    @InjectMocks
    private BrtServiceImpl brtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processCostFromHrsTest() throws NotFoundClientException {
        // Arrange
        BrtHistory brtHistory = new BrtHistory();
        brtHistory.setClient("79074437331");
        brtHistory.setCallerId("79025249522");
        brtHistory.setStartTime(LocalDateTime.now().minusMinutes(5));
        brtHistory.setEndTime(LocalDateTime.now());
        brtHistory.setCost(BigDecimal.ZERO);

        BrtHistory existingHistory = new BrtHistory();
        existingHistory.setClient("79074437331");
        existingHistory.setCallerId("79025249522");
        existingHistory.setStartTime(brtHistory.getStartTime());
        existingHistory.setEndTime(brtHistory.getEndTime());
        existingHistory.setCost(BigDecimal.TEN);

        BigDecimal cost = BigDecimal.valueOf(5.0);

        when(brtDatabaseService.findBrtHistoryByAttributes(
                brtHistory.getClient(), brtHistory.getCallerId(),
                brtHistory.getStartTime(), brtHistory.getEndTime()))
                .thenReturn(existingHistory);
        doNothing().when(brtDatabaseService).saveBrtHistoryToDatabase(any(BrtHistory.class));

        // Act
        brtService.processCostFromHrs(brtHistory, cost);

        // Assert
        verify(brtDatabaseService).saveBrtHistoryToDatabase(existingHistory);
        verify(balanceCalculatorService).calculateClientBalance("79074437331", cost);
    }

    @Test
    void processTariffChangeFromHrsTest() throws NotFoundClientException {
        // Arrange
        TariffPaymentHistory tariffPaymentHistory = new TariffPaymentHistory();
        tariffPaymentHistory.setClientId("79074437331");
        tariffPaymentHistory.setTariffId(12);
        tariffPaymentHistory.setCost(BigDecimal.valueOf(50.0));
        tariffPaymentHistory.setTime(LocalDateTime.now());

        Client client = new Client();
        client.setClientId("79074437331");
        client.setTariffId(11);
        client.setBalance(BigDecimal.valueOf(100000).setScale(2, RoundingMode.HALF_UP));

        when(brtDatabaseService.findClientById("79074437331")).thenReturn(client);
        doNothing().when(brtDatabaseService).saveTariffPaymentHistoryToDatabase(any(TariffPaymentHistory.class));
        doNothing().when(brtDatabaseService).saveClientToDatabase(any(Client.class));

        // Act
        brtService.processTariffChangeFromHrs(tariffPaymentHistory);

        // Assert
        verify(brtDatabaseService).saveTariffPaymentHistoryToDatabase(tariffPaymentHistory);
        verify(brtDatabaseService).saveClientToDatabase(client);
        assertEquals(12, client.getTariffId());
    }
}
