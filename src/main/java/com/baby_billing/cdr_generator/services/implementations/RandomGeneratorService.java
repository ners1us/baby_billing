package com.baby_billing.cdr_generator.services.implementations;

import com.baby_billing.cdr_generator.entities.Client;
import com.baby_billing.cdr_generator.repositories.IClientRepository;
import com.baby_billing.cdr_generator.services.IRandomGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class RandomGeneratorService implements IRandomGeneratorService {

    private final IClientRepository clientRepository;

    public Client getRandomClient() {
        List<Client> clients = clientRepository.findAll();
        Random random = new Random();
        return clients.get(random.nextInt(clients.size()));
    }

    public long generateRandomStartTime(long startTime, long endTime) {
        Random random = new Random();
        return startTime + (long) (random.nextDouble() * (endTime - startTime));
    }

    public long generateRandomEndTime(long startTime, long maxDuration) {
        Random random = new Random();
        return startTime + (long) (random.nextDouble() * maxDuration);
    }
}
