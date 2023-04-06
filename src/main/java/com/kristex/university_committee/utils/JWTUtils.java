package com.kristex.university_committee.utils;

import com.kristex.university_committee.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JWTUtils {

    private static final String SECRET_KEY = "mysecretkey";
    private static final long ACCESS_EXPIRATION_TIME_MS = 3600000; // 1 hour
    private static final long REFRESH_EXPIRATION_TIME_MS = 36000000; // 1 hour

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private static final byte[] SECRET_KEY_BYTES = DatatypeConverter.parseBase64Binary(SECRET_KEY);
    private static final Key SIGNING_KEY = new SecretKeySpec(SECRET_KEY_BYTES, SIGNATURE_ALGORITHM.getJcaName());

    /**
     * Create a JWT with the given user ID, role, password, and expiration time.
     *
     * @param userId the ID of the user associated with the JWT
     * @param userRole the role of the user associated with the JWT
     * @return the JWT
     */
    public static String createRefreshToken(String userId, Role userRole) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiration = new Date(nowMillis + REFRESH_EXPIRATION_TIME_MS);

        return Jwts.builder()
                .claim("userId", userId)
                .claim("userRole", userRole.toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SIGNATURE_ALGORITHM, SIGNING_KEY)
                .compact();
    }

    public static String createAccessToken(String userId, Role userRole) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiration = new Date(nowMillis + ACCESS_EXPIRATION_TIME_MS);

        return Jwts.builder()
                .claim("userId", userId)
                .claim("userRole", userRole.toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SIGNATURE_ALGORITHM, SIGNING_KEY)
                .compact();
    }

    /**
     * Determine whether the given JWT is valid.
     *
     * @param token the JWT to validate
     * @return true if the JWT is valid, false otherwise
     */
    public static boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SIGNING_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the user ID from the given JWT.
     *
     * @param token the JWT
     * @return the user ID
     * @throws Exception if the JWT is invalid or does not contain a user ID claim
     */
    public static String getUserIdFromToken(String token) throws Exception {
        Claims claims = Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
        if (claims == null) {
            throw new Exception("Invalid token: null claims");
        }
        String userId = claims.get("userId", String.class);
        if (userId == null) {
            throw new Exception("Invalid token: missing user ID claim");
        }
        return userId;
    }

    /**
     * Get the user role from the given JWT.
     *
     * @param token the JWT
     * @return the user role
     * @throws Exception if the JWT is invalid or does not contain a user role claim
     */
    public static String getUserRoleFromToken(String token) throws Exception {
        Claims claims = Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
        if (claims == null) {
            throw new Exception("Invalid token: null claims");
        }
        String userRole = claims.get("userRole", String.class);
        if (userRole == null) {
            throw new Exception("Invalid token: missing user role claim");
        }
        return userRole;
    }
}
