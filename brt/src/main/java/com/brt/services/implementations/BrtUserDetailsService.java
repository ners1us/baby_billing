package com.brt.services.implementations;

import com.brt.configs.BrtUserDetailsConfiguration;
import com.brt.entities.Client;
import com.brt.exceptions.NotFoundClientException;
import com.brt.repositories.BrtClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BrtUserDetailsService implements UserDetailsService {

    @Autowired
    private BrtClientRepository brtClientRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber){
        Optional<Client> client = brtClientRepository.findByClientId(phoneNumber);
        return client.map(BrtUserDetailsConfiguration::new)
                .orElseThrow(() -> new NotFoundClientException("Client not found"));
    }
}
