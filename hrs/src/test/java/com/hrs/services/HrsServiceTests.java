package com.hrs.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hrs.dto.BrtHistoryDto;
import com.hrs.entities.HrsHistory;
import com.hrs.entities.Tariffs;
import com.hrs.environments.HrsEnvironmentTest;
import com.hrs.models.TariffRules;
import com.hrs.publishers.HrsToBrtRabbitMQPublisher;
import com.hrs.repositories.HrsHistoryRepository;
import com.hrs.repositories.TrafficRepository;
import com.hrs.services.implementations.HrsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class HrsServiceTests extends HrsEnvironmentTest {

    @Mock
    private CallCostCalculator callCostCalculator;

    @Mock
    private HrsHistoryRepository historyRepository;

    @Mock
    private TrafficRepository trafficRepository;

    @Mock
    private HrsDatabaseService hrsDatabaseService;

    @Mock
    private HrsToBrtRabbitMQPublisher hrsToBrtRabbitMQPublisher;

    private HrsServiceImpl hrsService;

    private ObjectMapper objectMapper;

    private int currentMonth;

    @BeforeEach
    void setUp() {
        currentMonth = LocalDateTime.now().getMonthValue();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        hrsService = new HrsServiceImpl(
                callCostCalculator,
                historyRepository,
                trafficRepository,
                objectMapper,
                hrsDatabaseService,
                hrsToBrtRabbitMQPublisher
        );
    }

    @Test
    void processCallsFromBrtShouldSaveCallDataAndSendToRabbitMQTest() throws IOException {
        // Arrange
        BrtHistoryDto brtHistoryDto = new BrtHistoryDto();
        brtHistoryDto.setClient("79074437331");
        brtHistoryDto.setCallerId("79074437332");
        brtHistoryDto.setStartTime(LocalDateTime.now().minusMinutes(5));
        brtHistoryDto.setEndTime(LocalDateTime.now());
        brtHistoryDto.setTariffId(11);
        brtHistoryDto.setInternal(true);

        Tariffs tariff = new Tariffs();
        tariff.setTariffId(11);
        tariff.setTariffRules(new TariffRules());

        String jsonData = objectMapper.writeValueAsString(brtHistoryDto);

        when(hrsDatabaseService.getTariff(brtHistoryDto.getTariffId())).thenReturn(tariff);
        when(callCostCalculator.calculateDuration(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(300L);
        when(callCostCalculator.calculateCallCost(any(BrtHistoryDto.class), any(TariffRules.class), anyLong())).thenReturn(BigDecimal.valueOf(10.0));

        // Act
        hrsService.processCallsFromBrt(jsonData);

        // Assert
        verify(historyRepository, times(1)).save(any(HrsHistory.class));
        verify(trafficRepository, times(1)).findAll();
        verify(hrsDatabaseService, times(1)).saveCallData(eq(brtHistoryDto), eq(300L), eq(BigDecimal.valueOf(10.0)), eq(currentMonth));
        verify(hrsToBrtRabbitMQPublisher, times(1)).sendCallCostToBrt(eq(brtHistoryDto), eq(BigDecimal.valueOf(10.0)));
    }
}
