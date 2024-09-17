package com.hrs.services;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface HrsProcessorService {

    void processCallsFromBrt(String json) throws JsonProcessingException;
}
