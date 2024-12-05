package com.example.talent_api;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.context.annotation.Configuration;
import com.example.talent_api.models.Candidate;

import java.util.Base64;


@Configuration
public class KeyConfig {

    // Make sure secretKey is public and static
    public static SecretKey secretKey;

    @PostConstruct
    public void init() {
        // Load or generate your key securely
        String base64Key = "xg0rWWPV2cVNdn9UgKrxAv8QmtuALseYT/Biptw4VKM=";

        // Decode the Base64-encoded key to get the original 32-byte key
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);

        // Initialize the secretKey with the decoded bytes
        secretKey = new SecretKeySpec(keyBytes, "AES");
    }
}
