package com.brt.repositories;

import com.brt.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrtClientRepository extends JpaRepository<Client, String> {
}
