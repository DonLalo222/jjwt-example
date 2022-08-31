package com.example;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Hello world!
 *
 */
public class App {

    private static String secretKey(){
        return "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
    }

    private static String createToken() {
        // Key is hardcoded here for simplicity.
        // Ideally this will get loaded from env configuration/secret vault
        

        final Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey()),
                SignatureAlgorithm.HS256.getJcaName());

        Instant now = Instant.now();
        String jwtToken = Jwts.builder()
                .claim("name", "Jane Doe")
                .claim("email", "jane@example.com")
                .setSubject("jane")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(5l, ChronoUnit.MINUTES)))
                .signWith(hmacKey)
                .compact();

        return jwtToken;
    }

    public static Jws<Claims> parseJwt(String jwtString) {

        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey()), 
                                        SignatureAlgorithm.HS256.getJcaName());
    
        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(jwtString);
    
        return jwt;
    }

    public static void main(String[] args) {
        String token = App.createToken();
        
        System.out.println(token);

        Jws<Claims> parseToken = App.parseJwt(token);

        System.out.println(parseToken);
    }
}
