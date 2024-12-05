package com.example.talent_api.controllers;
import com.example.talent_api.models.User;
import com.example.talent_api.repositories.UserRepository;
import com.example.talent_api.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
public class ForgotPasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;


    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody Map<String, String> requestBody) {
        // Find the user by email
        User user = userRepository.findByUsername(requestBody.get("email"));
        if (user == null) {
            return "User not found!";
        }

        // Generate a reset token
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepository.save(user);

        // Generate a temporary password
        String temporaryPassword = UUID.randomUUID().toString().substring(0, 8); // Generates an 8-character password
        String hashedPassword = passwordEncoder.encode(temporaryPassword);
        user.setPassword(hashedPassword);
        userRepository.save(user);

        // Send the temporary password via email
        emailService.sendEmail(user.getUsername(), "Temporary Password",
                "Your temporary password is: " + temporaryPassword);

        return "A temporary password has been sent to your email!";
    }
}

