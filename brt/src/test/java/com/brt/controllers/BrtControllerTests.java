package com.brt.controllers;

import com.brt.entities.BrtHistory;
import com.brt.environments.BrtEnvironmentTest;
import com.brt.publishers.BrtToHrsRabbitMQPublisher;
import com.brt.services.BrtDatabaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BrtControllerTests extends BrtEnvironmentTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrtDatabaseService brtDatabaseService;

    @MockBean
    private BrtToHrsRabbitMQPublisher brtToHrsRabbitMQPublisher;

    @Test
    void sendHistoryToHrsShouldSendHistoryToHrsTest() throws Exception {
        // Arrange
        BrtHistory brtHistory = new BrtHistory();
        brtHistory.setId(1L);
        brtHistory.setClient("79074437331");
        brtHistory.setCallerId("79025249522");
        brtHistory.setStartTime(LocalDateTime.now().minusMinutes(10));
        brtHistory.setEndTime(LocalDateTime.now());
        brtHistory.setTariffId(11);
        brtHistory.setInternal(true);
        brtHistory.setCost(BigDecimal.valueOf(5).setScale(2, RoundingMode.HALF_UP));

        when(brtDatabaseService.findBrtHistoryById(1L)).thenReturn(brtHistory);
        doNothing().when(brtToHrsRabbitMQPublisher).sendCallToHrs(any(BrtHistory.class));

        // Act
        mockMvc.perform(post("/api/brt/sendHistoryToHrs")
                        .param("historyId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("History sent to Hrs successfully."));

        // Assert
        verify(brtDatabaseService, times(1)).findBrtHistoryById(1L);
        verify(brtToHrsRabbitMQPublisher, times(1)).sendCallToHrs(any(BrtHistory.class));
    }

    @Test
    void sendAllHistoriesToHrsShouldSendAllHistoriesToHrsTest() throws Exception {
        // Arrange
        List<BrtHistory> brtHistories = new ArrayList<>();
        BrtHistory brtHistory1 = new BrtHistory();
        brtHistory1.setId(1L);
        brtHistory1.setClient("79074437331");
        brtHistory1.setCallerId("79025249522");
        brtHistory1.setStartTime(LocalDateTime.now().minusMinutes(10));
        brtHistory1.setEndTime(LocalDateTime.now());
        brtHistory1.setTariffId(11);
        brtHistory1.setInternal(true);
        brtHistory1.setCost(BigDecimal.valueOf(5).setScale(2, RoundingMode.HALF_UP));

        BrtHistory brtHistory2 = new BrtHistory();
        brtHistory2.setId(2L);
        brtHistory2.setClient("79085342373");
        brtHistory2.setCallerId("79018936284");
        brtHistory2.setStartTime(LocalDateTime.now().minusMinutes(15));
        brtHistory2.setEndTime(LocalDateTime.now().minusMinutes(5));
        brtHistory2.setTariffId(12);
        brtHistory2.setInternal(false);
        brtHistory2.setCost(BigDecimal.valueOf(8.5).setScale(2, RoundingMode.HALF_UP));

        brtHistories.add(brtHistory1);
        brtHistories.add(brtHistory2);

        when(brtDatabaseService.getAllBrtHistories()).thenReturn(brtHistories);
        doNothing().when(brtToHrsRabbitMQPublisher).sendCallToHrs(any(BrtHistory.class));

        // Act
        mockMvc.perform(post("/api/brt/sendAllHistoriesToHrs"))
                .andExpect(status().isOk())
                .andExpect(content().string("All histories sent to Hrs successfully."));

        // Assert
        verify(brtDatabaseService, times(1)).getAllBrtHistories();
        verify(brtToHrsRabbitMQPublisher, times(2)).sendCallToHrs(any(BrtHistory.class));
    }

}