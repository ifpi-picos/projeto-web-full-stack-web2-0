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
        Date now = new Date(); //Obtém a data atual.

        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME); //Calcula a data de expiração do token adicionando o tempo de expiração ao tempo atual.

        return Jwts.builder() //Inicia a construção do token.
                .setSubject(username) //Define o assunto do token como o nome de usuário.
                .setIssuedAt(now) // Define a data de emissão do token como a data atual.
                .setExpiration(expiryDate)// Define a data de expiração do token.
                .signWith(secreteKey) //Assina o token com a chave secreta especificada 
                .compact(); // Compacta o token para uma string.
    }

    
    public String validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secreteKey)
                    .build()
                    .parseClaimsJws(token);
            return "valid";
            //utilizando a biblioteca io.jsonwebtoken.Jwts para realizar a análise do token. O método parseClaimsJws(token) faz a análise e verificação do token. Se o token for válido, esse método não lançará exceções. Caso contrário, lançará uma exceção.
        } catch (ExpiredJwtException ex) {
            // Token xepirado
            return "expired token";
        } catch (JwtException | IllegalArgumentException e) {
            // Token invalido (falhha token)
            return "invalid token";
        }
    }


    
}



/*
 io.jsonwebtoken: Esta biblioteca é usada para criação e verificação de tokens JWT.

 org.springframework.stereotype.Component: Esta anotação é usada para indicar que uma classe é um componente do Spring, permitindo que o Spring gerencie objetos desta classe e os injete quando necessário.

 */