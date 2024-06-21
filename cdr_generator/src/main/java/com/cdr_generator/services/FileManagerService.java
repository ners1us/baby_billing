package com.cdr_generator.services;

import com.cdr_generator.entities.CdrHistory;

import java.io.IOException;
import java.util.List;

public interface FileManagerService {

    void checkAndCleanDataFolder() throws IOException;

    List<CdrHistory> readHistoryFromFile() throws IOException;

    List<List<CdrHistory>> splitIntoFiles(List<CdrHistory> cdrHistoryList, int maxCallsPerFile);

    void saveCdrToFile(List<CdrHistory> cdrHistoryList, String fileName);
}
