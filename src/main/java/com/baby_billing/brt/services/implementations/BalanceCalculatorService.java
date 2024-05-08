package com.baby_billing.brt.services.implementations;

import com.baby_billing.brt.entities.Client;
import com.baby_billing.brt.repositories.IBrtClientRepository;
import com.baby_billing.brt.services.IBalanceCalculatorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class BalanceCalculatorService implements IBalanceCalculatorService {

    private IBrtClientRepository clientRepository;

    public void calculateClientBalance(String clientId, BigDecimal cost) {
        Client client = clientRepository.findById(clientId).orElse(null);

        if (client == null) {
            throw new RuntimeException("Client not found");
        } else {
            BigDecimal newBalance = client.getBalance().subtract(cost);
            client.setBalance(newBalance);

            clientRepository.save(client);
        }
    }
}
