package com.github.fiecher.turnforge.infrastructure.security;

import com.github.fiecher.turnforge.domain.models.UserRole;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

public class TokenService {

    private static final String HMAC_ALGO = "HmacSHA256";
    private final String secret;

    public TokenService(String salt) {
        if (salt == null || salt.length() < 16) {
            throw new IllegalArgumentException("Salt must be at least 16 characters long for security.");
        }
        this.secret = salt;
    }

    public String generateToken(Long userId, UserRole role) {
        long expirationTime = Instant.now().plusSeconds(86400).getEpochSecond();
        String payload = userId + ":" + role.name() + ":" + expirationTime;

        String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        String signature = sign(encodedPayload);

        return encodedPayload + "." + signature;
    }

    public AuthData validateAndParse(String token) {
        try {
            if (token == null || !token.contains(".")) return null;

            String[] parts = token.split("\\.");
            if (parts.length != 2) return null;

            String encodedPayload = parts[0];
            String signature = parts[1];

            String expectedSignature = sign(encodedPayload);
            if (!expectedSignature.equals(signature)) {
                return null;
            }

            String payload = new String(Base64.getUrlDecoder().decode(encodedPayload), StandardCharsets.UTF_8);
            String[] data = payload.split(":");

            if (data.length != 3) return null;

            long exp = Long.parseLong(data[2]);
            if (Instant.now().getEpochSecond() > exp) {
                return null;
            }

            return new AuthData(Long.parseLong(data[0]), UserRole.valueOf(data[1]));

        } catch (Exception e) {
            return null;
        }
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGO);
            mac.init(secretKeySpec);
            byte[] signedBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(signedBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error signing token", e);
        }
    }

    public record AuthData(Long userId, UserRole role) {}
}