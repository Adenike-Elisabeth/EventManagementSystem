package com.nike.eventmanagementsystem.repositories;

import com.nike.eventmanagementsystem.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAddress( String emailAddress);
}
