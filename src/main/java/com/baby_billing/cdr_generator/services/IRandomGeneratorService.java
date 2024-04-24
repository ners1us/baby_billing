package com.baby_billing.cdr_generator.services;

import com.baby_billing.cdr_generator.entities.Client;

public interface IRandomGeneratorService {

    Client getRandomClient();

    long generateRandomStartTime(long startTime, long endTime);

    long generateRandomEndTime(long startTime, long maxDuration);

    int generateRandomNumberOfCalls();
}
