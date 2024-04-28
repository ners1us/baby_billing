package com.baby_billing.cdr_generator.services.implementations;

import com.baby_billing.cdr_generator.entities.Client;
import com.baby_billing.cdr_generator.repositories.IClientRepository;
import com.baby_billing.cdr_generator.services.IRandomGeneratorService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RandomGeneratorService implements IRandomGeneratorService {

    @NonNull
    private final IClientRepository clientRepository;
    private final Random random = new Random();

    public Client getRandomClient() {
        List<Client> clients = clientRepository.findAll();
        return clients.get(random.nextInt(clients.size()));
    }

    public long generateRandomStartTime(long startTime, long endTime) {
        return startTime + (long) (random.nextDouble() * (endTime - startTime));
    }

    public long generateRandomEndTime(long startTime, long maxDuration) {
        return startTime + (long) (random.nextDouble() * maxDuration);
    }

    public int generateRandomNumberOfCalls() {
        return random.nextInt(10) + 1;
    }
}
