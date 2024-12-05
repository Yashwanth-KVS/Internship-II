package com.example.talent_api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.talent_api.models.Credentials;
import com.example.talent_api.models.User;
import com.example.talent_api.repositories.UserRepository;

@RestController
public class RegisterController {
    @Autowired
    UserRepository userRepository;

    // Create an instance of BCryptPasswordEncoder
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Credentials credentials) {
        // Check if username already exists
        if (userRepository.findByUsername(credentials.getUsername()) != null) {
            return ResponseEntity.status(400).body("Username already exists");
        }

        // Create a new User entity
        User newUser = new User();
        newUser.setUsername(credentials.getUsername());

        String encodedPassword = passwordEncoder.encode(credentials.getPassword());
        newUser.setPassword(encodedPassword);

//        newUser.setPassword(credentials.getPassword());

        // Save the new user to the repository (database)
        userRepository.save(newUser);

        // Respond with a success message
        return ResponseEntity.status(201).body("User registered successfully");
    }
}
