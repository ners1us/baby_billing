package com.cdr_generator.services;

import com.cdr_generator.entities.History;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CdrService {

    void processCdr(List<History> historyList);

    CompletableFuture<List<History>> generateCdr();

    List<History> readHistory() throws IOException;

    void checkAndCleanData() throws IOException;
}
