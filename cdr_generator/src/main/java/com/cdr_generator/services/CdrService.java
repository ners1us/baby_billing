package com.cdr_generator.services;

import com.cdr_generator.entities.CdrHistory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CdrService {

    void processCdr(List<CdrHistory> cdrHistoryList);

    CompletableFuture<List<CdrHistory>> generateCdr();

    List<CdrHistory> readHistory() throws IOException;

    void checkAndCleanData() throws IOException;
}
