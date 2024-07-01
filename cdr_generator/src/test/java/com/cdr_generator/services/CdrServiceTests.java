package com.cdr_generator.services;

import com.cdr_generator.entities.CdrHistory;
import com.cdr_generator.environments.CdrEnvironmentTest;
import com.cdr_generator.services.implementations.CdrServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CdrServiceTests extends CdrEnvironmentTest {

    @Mock
    private FileManagerService fileManagerService;

    @InjectMocks
    private CdrServiceImpl cdrService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void readHistoryTest() throws IOException {
        // Arrange
        List<CdrHistory> expectedHistory = new ArrayList<>();
        when(fileManagerService.readHistoryFromFile()).thenReturn(expectedHistory);

        // Act
        List<CdrHistory> result = cdrService.readHistory();

        // Assert
        assertEquals(expectedHistory, result);
        verify(fileManagerService, times(1)).readHistoryFromFile();
    }

    @Test
    void checkAndCleanDataTest() throws IOException {
        // Act
        cdrService.checkAndCleanData();

        // Assert
        verify(fileManagerService, times(1)).checkAndCleanDataFolder();
    }
}
