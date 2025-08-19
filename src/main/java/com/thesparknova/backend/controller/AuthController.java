package com.thesparknova.backend.controller;

import com.thesparknova.backend.model.User;
import com.thesparknova.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"https://frontend-34c4.onrender.com", "http://localhost:3000"})
public class AuthController {
    @Autowired private UserService userSvc;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User u) {
        try {
            User saved = userSvc.register(u);
            saved.setPassword(null);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String pass = body.get("password");

        return userSvc.login(email, pass)
            .<ResponseEntity<?>>map(u -> {
                u.setPassword(null);
                return ResponseEntity.ok(u);
            })
            .orElseGet(() -> ResponseEntity.status(401)
                .body(Map.of("message", "Invalid credentials")));
    }

}
