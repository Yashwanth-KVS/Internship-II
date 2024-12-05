package com.example.talent_api.controllers;

import java.util.List;
import java.util.Optional;

import com.example.talent_api.KeyConfig;
import com.example.talent_api.services.AesEncryptionService;
import com.example.talent_api.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.talent_api.models.Candidate;
import com.example.talent_api.repositories.CandidateRepository;
import com.example.talent_api.repositories.UserRepository;

@RestController
@RequestMapping("/candidates")
public class CandidateController {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired UserRepository userRepository;

    @Autowired
    private AesEncryptionService aesEncryptionService;

    @Autowired
    private CandidateService candidateService ;

    // Get all candidates
    @GetMapping
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        List<Candidate> candidates = candidateRepository.findAll();

        if (candidates.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Decrypt the phone number for each candidate
        candidates.forEach(candidate -> {
            try {
                if (candidate.getPhone() != null) {
                    String decryptedPhone = aesEncryptionService.decrypt(candidate.getPhone(), KeyConfig.secretKey);
                    candidate.setPhone(decryptedPhone);
                }

                if (candidate.getAddress() != null) {
                    String decryptedAddress = aesEncryptionService.decrypt(candidate.getAddress(), KeyConfig.secretKey);
                    candidate.setAddress(decryptedAddress);
                }

                if (candidate.getResume() != null) {
                    String decryptedResumeLink = aesEncryptionService.decrypt(candidate.getResume(), KeyConfig.secretKey);
                    candidate.setResume(decryptedResumeLink);
                }

            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error decrypting phone number");
            }
        });

        return ResponseEntity.ok(candidates);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable int id) {
        Optional<Candidate> candidate = candidateRepository.findById(id);

        return candidate.map(c -> {
            try {
                // Decrypt the phone number
                if (c.getPhone() != null) {
                    String decryptedPhone = aesEncryptionService.decrypt(c.getPhone(), KeyConfig.secretKey);
                    c.setPhone(decryptedPhone);
                }

                if (c.getAddress() != null) {
                    String decryptedAddress = aesEncryptionService.decrypt(c.getAddress(), KeyConfig.secretKey);
                    c.setAddress(decryptedAddress);
                }

                if (c.getResume() != null) {
                    String decryptedResumeLink = aesEncryptionService.decrypt(c.getResume(), KeyConfig.secretKey);
                    c.setResume(decryptedResumeLink);
                }

            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error decrypting phone number");
            }
            return ResponseEntity.ok(c);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Create a new candidate
    @PostMapping
    public ResponseEntity<Candidate> createCandidate(@RequestBody Candidate candidate) {
        if (!userRepository.existsById(candidate.getUserId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }

        try {
            // Encrypt the phone number before saving
            if (candidate.getPhone() != null) {
                String encryptedPhone = aesEncryptionService.encrypt(candidate.getPhone(), KeyConfig.secretKey);
                candidate.setPhone(encryptedPhone);
            }

            if (candidate.getAddress() != null) {
                String encryptedAddress = aesEncryptionService.encrypt(candidate.getAddress(), KeyConfig.secretKey);
                candidate.setAddress(encryptedAddress);
            }

            if (candidate.getResume() != null) {
                String encryptedResumeLink = aesEncryptionService.encrypt(candidate.getResume(), KeyConfig.secretKey);
                candidate.setResume(encryptedResumeLink);
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error encrypting phone number");
        }

        Candidate savedCandidate = candidateRepository.save(candidate);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCandidate);
    }


    // Update an existing candidate
    @PutMapping("/{id}")
    public ResponseEntity<Candidate> updateCandidate(@PathVariable int id, @RequestBody Candidate candidate) {
        if (!candidateRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Encrypt the phone number before saving
            if (candidate.getPhone() != null) {
                String encryptedPhone = aesEncryptionService.encrypt(candidate.getPhone(), KeyConfig.secretKey);
                candidate.setPhone(encryptedPhone);
            }

            if (candidate.getAddress() != null) {
                String encryptedAddress = aesEncryptionService.encrypt(candidate.getAddress(), KeyConfig.secretKey);
                candidate.setAddress(encryptedAddress);
            }

            if (candidate.getResume() != null) {
                String encryptedResumeLink = aesEncryptionService.encrypt(candidate.getResume(), KeyConfig.secretKey);
                candidate.setResume(encryptedResumeLink);
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error encrypting phone number");
        }

        candidate.setId(id);
        Candidate updatedCandidate = candidateRepository.save(candidate);
        return ResponseEntity.ok(updatedCandidate);
    }


    // Delete a candidate
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable int id) {
        if (!candidateRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        candidateRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
