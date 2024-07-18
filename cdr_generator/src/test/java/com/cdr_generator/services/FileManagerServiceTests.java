package com.cdr_generator.services;

import com.cdr_generator.entities.CdrHistory;
import com.cdr_generator.environments.CdrEnvironmentTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class FileManagerServiceTests extends CdrEnvironmentTest {

    @Mock
    private FileManagerService fileManagerService;

    @Test
    void readHistoryTest() throws IOException {
        // Arrange
        List<CdrHistory> expectedHistory = new ArrayList<>();
        when(fileManagerService.readHistoryFromFile()).thenReturn(expectedHistory);

        // Act
        List<CdrHistory> result = fileManagerService.readHistoryFromFile();

        // Assert
        assertEquals(expectedHistory, result);
        verify(fileManagerService, times(1)).readHistoryFromFile();
    }

    @Test
    void checkAndCleanDataTest() throws IOException {
        // Act
        fileManagerService.checkAndCleanDataFolder();

        // Assert
        verify(fileManagerService, times(1)).checkAndCleanDataFolder();
    }
}
