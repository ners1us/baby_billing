package com.cdr_generator.services;

import com.cdr_generator.entities.Client;

public interface RandomGeneratorService {

    Client getRandomClient();

    long generateRandomStartTime(long startTime, long endTime);

    long generateRandomEndTime(long startTime, long maxDuration);

    int generateRandomNumberOfCalls();
}
