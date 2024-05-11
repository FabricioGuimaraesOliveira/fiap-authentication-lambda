package com.fiap.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class JwtTokenUtil {

    private JwtTokenUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    private static final Duration EXPIRATION_DURATION = Duration.ofHours(1); // Expiration time: 1 hour

    public static String generateToken(String cpf) {
        Instant expirationTime = Instant.now().plus(EXPIRATION_DURATION);
        Date expiryDate = Date.from(expirationTime);

        return Jwts.builder()
                .claim("cpf", cpf)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }
}
