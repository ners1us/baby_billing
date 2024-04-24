package com.baby_billing.cdr_generator.services.implementations;

import com.baby_billing.cdr_generator.entities.History;
import com.baby_billing.cdr_generator.repositories.IHistoryRepository;
import com.baby_billing.cdr_generator.services.IDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DatabaseService implements IDatabaseService {

    private IHistoryRepository historyRepository;

    public void saveCdrToDatabase(List<History> historyList) {
        historyRepository.saveAll(historyList);
    }
}
