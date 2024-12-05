package com.example.talent_api;


import java.security.SecureRandom;
import java.util.Base64;

public class KeyGeneratorUtil  {
    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 32 bytes for a 256-bit key
        secureRandom.nextBytes(keyBytes);

        // Convert the key to a Base64-encoded string
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("Generated Key (Base64): " + base64Key);
    }
}
