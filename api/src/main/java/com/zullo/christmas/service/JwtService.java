package com.zullo.christmas.service;

import java.security.Key;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zullo.christmas.model.Database.User;
import com.zullo.christmas.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    Logger LOG = LoggerFactory.getLogger(JwtService.class);

    private final Key secretKey;
    private UserRepository userRepository;


    public JwtService(@Value("${spring.security.jwt.secretKey}") String secretKey, UserRepository userRepository){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.userRepository = userRepository;
    }

    public String generateJwt(User user){
        return Jwts.builder()
            .setSubject(user.getUsername())
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
            .claim("roles", user.getRole())
            .signWith(secretKey)
            .compact();
    }

    public User extractUserFromJwt(String jwt){
        Claims parsedJwt = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(jwt)
            .getBody();

        //User user = userRepository.getUserByUsername(username);
        User user = new User();
        user.setUsername(parsedJwt.getSubject());
        user.setRole(parsedJwt.get("roles", Character.class));

        return user;
    }

}
