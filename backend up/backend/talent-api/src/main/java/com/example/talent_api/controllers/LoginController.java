package com.example.talent_api.controllers;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import org.springframework.web.bind.annotation.*;
import java.security.GeneralSecurityException;
import com.google.api.client.json.JsonFactory;

import java.security.GeneralSecurityException;

import com.example.talent_api.models.Candidate;
import com.example.talent_api.models.Credentials;
import com.example.talent_api.models.Manager;
import com.example.talent_api.models.User;
import com.example.talent_api.repositories.CandidateRepository;
import com.example.talent_api.repositories.ManagerRepository;
import com.example.talent_api.repositories.UserRepository;


import com.google.api.client.json.gson.GsonFactory; // Use GsonFactory

import java.util.HashMap;

@RestController
public class LoginController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
    ManagerRepository managerRepository;

    private static final String CLIENT_ID = "1045431858909-5dmen895jtbjsmrqa09ebkggd5tpglro.apps.googleusercontent.com"; // Replace with your client ID
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance(); // Use GsonFactory

    // Create an instance of BCryptPasswordEncoder
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Credentials credentials) {
        User user = userRepository.findByUsername(credentials.getUsername());

        if (user == null || !passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
//        if (user == null) {
//            return ResponseEntity.status(401).body("Invalid credentials");
//        }

        if (passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("userType", user.getType());
            
            if (user.getType().equalsIgnoreCase("Candidate")) {
                Optional<Candidate> candidate = candidateRepository.findByUserId(user.getId());
                if (candidate.isPresent()) {
                    response.put("candidate", candidate.get());
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.status(404).body("Candidate profile not found");
                }
            } else if (user.getType().equalsIgnoreCase("Manager")) {
                Optional<Manager> manager = managerRepository.findByUserId(user.getId());
                if (manager.isPresent()) {
                    response.put("manager", manager.get());
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.status(404).body("Manager profile not found");
                }
            } else {
                response.put("user", user);
                return ResponseEntity.ok(response);
            }
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> tokenMap) {
        String token = tokenMap.get("token");

        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY)
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Extract user details from the payload
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                // Check if the user already exists in your database
                User user = userRepository.findByUsername(email); // Using email as the username
                if (user == null) {
                    // Register a new user if necessary
                    user = new User();
                    user.setUsername(email); // Set email as username
                    user.setPassword(""); // Password not required for Google login
                    user.setType("Candidate"); // Set default user type, modify as needed

                    // Save the user to generate and set the ID
                    user = userRepository.save(user); // JPA will auto-generate and set the ID

                    // Check userType and create corresponding Candidate or Manager entry
                    if (user.getType().equalsIgnoreCase("Candidate")) {
                        Candidate candidate = new Candidate();
                        candidate.setUserId(user.getId());
                        candidate.setFullName(name);
                        candidate.setEmail(email);
                        candidateRepository.save(candidate); // Save the Candidate to the database
                    } else if (user.getType().equalsIgnoreCase("Manager")) {
                        Manager manager = new Manager();
                        manager.setUserId(user.getId());
                        manager.setFullName(name);
                        manager.setEmail(email);
                        managerRepository.save(manager); // Save the Manager to the database
                    }
                }

                // Prepare response
                Map<String, Object> response = new HashMap<>();
                response.put("userType", user.getType());
                response.put("user", user);

                // Fetch the Candidate or Manager details
                if (user.getType().equalsIgnoreCase("Candidate")) {
                    Optional<Candidate> candidate = candidateRepository.findByUserId(user.getId());
                    if (candidate.isPresent()) {
                        response.put("candidate", candidate.get());
                    }
                } else if (user.getType().equalsIgnoreCase("Manager")) {
                    Optional<Manager> manager = managerRepository.findByUserId(user.getId());
                    if (manager.isPresent()) {
                        response.put("manager", manager.get());
                    }
                }

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body("Invalid ID token");
            }
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.status(500).body("Token verification failed: " + e.getMessage());
        }
    }


    // @PostMapping("/login")
	// public ResponseEntity<User> create(@RequestBody Credentials credentials) {
	// User user = userRepository.findByUsername(credentials.getUsername());
	// if(user == null) {
	// return ResponseEntity.status(401).body(null);
	// }
	// if(user.getPassword().equalsIgnoreCase(credentials.getPassword() )) {
	// return ResponseEntity.status(200).body(user);
	// }else {
	// return ResponseEntity.status(401).body(null);
	// }
	// }

}

