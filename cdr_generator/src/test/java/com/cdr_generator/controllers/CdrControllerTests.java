package com.cdr_generator.controllers;

import com.cdr_generator.dto.CdrHistoryDto;
import com.cdr_generator.entities.CdrHistory;
import com.cdr_generator.entities.Client;
import com.cdr_generator.environments.CdrEnvironmentTest;
import com.cdr_generator.publishers.CdrToBrtRabbitMQPublisher;
import com.cdr_generator.services.CdrManagerService;
import com.cdr_generator.services.FileManagerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CdrControllerTests extends CdrEnvironmentTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CdrManagerService cdrManagerService;

    @MockBean
    private FileManagerService fileManagerService;

    @MockBean
    private CdrToBrtRabbitMQPublisher cdrToBrtRabbitMQPublisher;

    @Test
    void generateAndSaveCdrTest() throws Exception {
        // Arrange
        List<CdrHistory> cdrHistoryList = new ArrayList<>();

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
        cdrHistory.setEndTime(1680307300L);
        cdrHistoryList.add(cdrHistory);

        // Act
        when(cdrManagerService.generateCdr()).thenReturn(CompletableFuture.completedFuture(cdrHistoryList));
        doNothing().when(fileManagerService).checkAndCleanDataFolder();
        doNothing().when(cdrManagerService).processCdr(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cdr_generator/generateCdr")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Assert
        verify(fileManagerService, times(1)).checkAndCleanDataFolder();
        verify(cdrManagerService, times(1)).generateCdr();
        verify(cdrManagerService, times(1)).processCdr(any());
    }

    @Test
    void publishCdrTest() throws Exception {
        // Arrange
        List<CdrHistory> cdrHistoryList = new ArrayList<>();

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
        cdrHistory.setEndTime(1680307300L);
        cdrHistoryList.add(cdrHistory);

        // Act
       when(fileManagerService.readHistoryFromFile()).thenReturn(cdrHistoryList);

       mockMvc.perform(MockMvcRequestBuilders.post("/api/cdr_generator/publishCdr")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Messages sent to RabbitMQ..."));

        // Assert
        verify(cdrToBrtRabbitMQPublisher).sendMessage(CdrHistoryDto.fromEntity(cdrHistory));
    }
}
