package com.brt.repositories;

import com.brt.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrtClientRepository extends JpaRepository<Client, String> {

    Optional<Client> findByClientId(String phoneNumber);
}
