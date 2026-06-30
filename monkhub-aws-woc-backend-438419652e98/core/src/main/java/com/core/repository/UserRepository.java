package com.core.repository;

import com.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsernameAndIsActive(String username, Boolean isActive);

    boolean existsByEmailAndIsActive(String email, Boolean isActive);

    boolean existsByUsernameAndIsActive(String username, Boolean isActive);



}
