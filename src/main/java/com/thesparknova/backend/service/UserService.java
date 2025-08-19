package com.thesparknova.backend.service;

import com.thesparknova.backend.model.User;
import com.thesparknova.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    @Autowired 
    private UserRepository userRepo;

    @Autowired 
    private BCryptPasswordEncoder encoder;

    public User register(User u) {
        if (userRepo.findByEmail(u.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        u.setPassword(encoder.encode(u.getPassword()));
        return userRepo.save(u);
    }

    public Optional<User> login(String email, String rawPassword) {
        return userRepo.findByEmail(email)
            .filter(u -> encoder.matches(rawPassword, u.getPassword()));
    }
}
