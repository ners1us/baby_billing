package com.cdr_generator.services;

import com.cdr_generator.entities.CdrHistory;

public interface RandomGeneratorService {

    CdrHistory generateRandomCall(long startTime, long duration);

    int generateRandomNumberOfCalls();
}
