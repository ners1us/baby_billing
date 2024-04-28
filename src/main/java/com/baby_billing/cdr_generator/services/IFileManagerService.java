package com.baby_billing.cdr_generator.services;

import com.baby_billing.cdr_generator.entities.History;

import java.io.IOException;
import java.util.List;

public interface IFileManagerService {

    void checkAndCleanDataFolder() throws IOException;

    List<History> readHistoryFromFile() throws IOException;

    List<List<History>> splitIntoFiles(List<History> historyList, int maxCallsPerFile);

    void saveCdrToFile(List<History> historyList, String fileName);
}
