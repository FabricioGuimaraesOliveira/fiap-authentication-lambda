package com.fiap.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

public class JwtTokenUtil {

    private JwtTokenUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String generateToken(String cpf) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("fiap");

            return JWT.create()
                    .withClaim("cpf", cpf)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
