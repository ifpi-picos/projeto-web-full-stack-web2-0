package com.JWTpostegree.demo.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key; //Utilizado para representar uma chave criptográfica.
import java.util.Date;

@Component
public class JwtTokenUtil {

    private final Key secreteKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long EXPIRATION_TIME = 60000; //1 min

    public String generateToken(String username) { //Este método é responsável por gerar um token JWT:
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secreteKey)
                .compact();
    }
    public String validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secreteKey)
                    .build()
                    .parseClaimsJws(token);
            return "valid";
        } catch (ExpiredJwtException ex) {
            // Token is expired
            return "expired token";
        } catch (JwtException | IllegalArgumentException e) {
            // Token is invalid (failed parsing or verification)
            return "invalid token";
        }
    }


    
}



/*
 io.jsonwebtoken: Esta biblioteca é usada para criação e verificação de tokens JWT.

 org.springframework.stereotype.Component: Esta anotação é usada para indicar que uma classe é um componente do Spring, permitindo que o Spring gerencie objetos desta classe e os injete quando necessário.

 */