package com.baby_billing.hrs.services;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IHrsService {

    void processCallsFromBrt(String json) throws JsonProcessingException;
}
