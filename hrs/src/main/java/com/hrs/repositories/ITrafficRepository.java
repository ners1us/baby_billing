package com.hrs.repositories;

import com.hrs.entities.Traffic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITrafficRepository extends JpaRepository<Traffic, Long> {

    Traffic findByClientId(String clientId);
}
