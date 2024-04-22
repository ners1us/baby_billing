package com.baby_billing.cdr_generator.services;

import com.baby_billing.cdr_generator.entities.History;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ICdrService {
    void processCdr(List<History> historyList);
    CompletableFuture<List<History>> generateCdr();
}
