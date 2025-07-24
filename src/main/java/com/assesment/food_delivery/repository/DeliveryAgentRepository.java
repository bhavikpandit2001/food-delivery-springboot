package com.assesment.food_delivery.repository;

import com.assesment.food_delivery.entity.DeliveryAgent;
import com.assesment.food_delivery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, Long> {
    List<DeliveryAgent> findByIsAvailableTrue();

    Optional<DeliveryAgent> findByEmail(String email);

}
