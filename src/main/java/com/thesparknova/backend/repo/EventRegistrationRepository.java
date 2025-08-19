package com.thesparknova.backend.repo;

import com.thesparknova.backend.model.EventRegistration;
import com.thesparknova.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    List<EventRegistration> findByUser(User user);
    List<EventRegistration> findByUserOrderByRegisteredAtDesc(User user);
}
