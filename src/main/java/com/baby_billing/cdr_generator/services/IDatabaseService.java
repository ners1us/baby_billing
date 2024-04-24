package com.baby_billing.cdr_generator.services;

import com.baby_billing.cdr_generator.entities.History;

import java.util.List;

public interface IDatabaseService {

    void saveCdrToDatabase(List<History> historyList);
}
