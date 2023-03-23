package com.kristex.university_committee.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import jakarta.xml.bind.DatatypeConverter;

public class JWTTokenCreator {

    private static final String SECRET_KEY = "your-secret-key";
    private static final long REFRESH_EXPIRATION_TIME = 864_000_000; // 10 days
    private static final long ACCESS_EXPIRATION_TIME = 3_600_000; // 1 hour

    public static String createRefreshToken(String subject) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + REFRESH_EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static String createAccessToken(String subject) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + ACCESS_EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}

