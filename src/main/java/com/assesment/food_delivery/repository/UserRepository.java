package com.assesment.food_delivery.repository;

import com.assesment.food_delivery.entity.User;
import com.assesment.food_delivery.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.management.relation.Role;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(UserRole role);

}
