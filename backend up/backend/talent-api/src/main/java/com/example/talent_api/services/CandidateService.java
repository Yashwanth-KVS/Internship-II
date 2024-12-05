package com.example.talent_api.services;

import com.example.talent_api.models.Candidate;
import com.example.talent_api.KeyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class CandidateService {



    @Autowired
    private AesEncryptionService aesEncryptionService;

    public void encryptPhoneNumber(Candidate candidate) {
        try {
            if (KeyConfig.secretKey != null) {
                String encryptedPhone = aesEncryptionService.encrypt(candidate.getPhone(), KeyConfig.secretKey);
                candidate.setPhone(encryptedPhone);
            } else {
                throw new IllegalStateException("SecretKey is not initialized");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting phone number", e);
        }
    }

    public void decryptPhoneNumber(Candidate candidate) {
        try {
            if (KeyConfig.secretKey != null) {
                String decryptedPhone = aesEncryptionService.decrypt(candidate.getPhone(), KeyConfig.secretKey);
                candidate.setPhone(decryptedPhone);
            } else {
                throw new IllegalStateException("SecretKey is not initialized");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting phone number", e);
        }
    }
}
