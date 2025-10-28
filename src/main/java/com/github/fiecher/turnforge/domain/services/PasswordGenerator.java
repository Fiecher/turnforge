package com.github.fiecher.turnforge.domain.services;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Objects;

public class PasswordGenerator {
    private static PasswordGenerator INSTANCE;
    private final byte[] salt;

    private PasswordGenerator(String salt) {
        Objects.requireNonNull(salt, "Salt cannot be null");
        if (salt.trim().isEmpty()) {
            throw new IllegalArgumentException("Salt cannot be empty");
        }
        this.salt = salt.getBytes(StandardCharsets.UTF_8);
    }

    public static PasswordGenerator init(String salt) {
        if (INSTANCE != null) {
            throw new AssertionError("PasswordGenerator is already initialized");
        }

        INSTANCE = new PasswordGenerator(salt);
        return INSTANCE;
    }

    public static PasswordGenerator getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("PasswordGenerator not initialized. Call init() first with your salt");
        }

        return INSTANCE;
    }

    public String hash(String password) {
        try {
            int iterations = 100000;
            int keyLength = 256;
            char[] passwordChars = password.toCharArray();

            PBEKeySpec spec = new PBEKeySpec(passwordChars, salt, iterations, keyLength);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();

            return String.format("PBKDF2WithHmacSHA256$%d$%s$%s",
                    iterations,
                    Base64.getEncoder().encodeToString(salt),
                    Base64.getEncoder().encodeToString(hash)
            );
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    public boolean verify(String password, String hashedPassword) {
        try {
            String[] parts = hashedPassword.split("\\$");
            if (parts.length != 4 || !parts[0].equals("PBKDF2WithHmacSHA256")) {
                throw new IllegalArgumentException("Invalid hashed password format");
            }

            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[3]);

            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, expectedHash.length * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] actualHash = skf.generateSecret(spec).getEncoded();

            return MessageDigest.isEqual(expectedHash, actualHash);
        } catch (Exception e) {
            return false;
        }
    }

    public static String generateSalt(int length) {
        byte[] salt = new byte[length];
        new java.security.SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

}