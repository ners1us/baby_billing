package com.baby_billing.brt.repositories;

import com.baby_billing.brt.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBrtClientRepository extends JpaRepository<Client, String> {
}
