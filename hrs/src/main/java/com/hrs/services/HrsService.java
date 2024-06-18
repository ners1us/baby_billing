package com.hrs.services;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface HrsService {

    void processCallsFromBrt(String json) throws JsonProcessingException;
}
