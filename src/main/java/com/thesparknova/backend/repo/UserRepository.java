package com.thesparknova.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.thesparknova.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
