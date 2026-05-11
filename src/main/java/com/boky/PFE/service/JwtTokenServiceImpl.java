package com.boky.PFE.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private final String secret;

    public JwtTokenServiceImpl(@Value("${jwt.secret:SECRET}") String secret) {
        this.secret = secret;
    }

    @Override
    public String createTokenForUserData(Object data) {
        return Jwts.builder()
                .claim("data", data)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}
