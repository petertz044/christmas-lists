package com.zullo.christmas.service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zullo.christmas.model.database.User;
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
        LOG.debug("Generating a JWT for user", user.getUsername());
        return Jwts.builder()
            .setSubject(String.valueOf(user.getId()))
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
            .claim("roles", user.getRole())
            .signWith(secretKey)
            .compact();
    }

    public User extractUserFromJwt(String jwt){
        LOG.debug("Parsing JWT...");
        Claims parsedJwt = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(jwt)
            .getBody();

        //User user = userRepository.getUserByUsername(username);
        User user = new User();
        user.setId(Integer.valueOf(parsedJwt.getSubject()));
        user.setRole(parsedJwt.get("roles", String.class));

        return user;
    }

}
