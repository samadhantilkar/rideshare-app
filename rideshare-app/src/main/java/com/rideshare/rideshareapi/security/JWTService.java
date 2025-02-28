package com.rideshare.rideshareapi.security;

import com.rideshare.rideshareapi.account.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Account account){
        return Jwts.builder()
                .subject(account.getId().toString())
                .claim("Email",account.getEmail())
                .claim("roles",account.getRoles())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*10*6))
                .signWith(getSecretKey())
                .compact();
    }

    public String generateRefreshToken(Account account){
        return Jwts.builder()
                .subject(String.valueOf(account.getId()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000L*60*60*24*30*6))
                .signWith(getSecretKey())
                .compact();
    }

    public Long getAccountIdFromToken(String token){
        Claims claims=Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.valueOf(claims.getSubject());
    }
}