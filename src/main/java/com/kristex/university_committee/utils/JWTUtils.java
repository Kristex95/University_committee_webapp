package com.kristex.university_committee.utils;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kristex.university_committee.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class JWTUtils {
    private static final Logger log = Logger.getLogger(JWTUtils.class);

    private static final String SECRET_KEY = "mysecretkey";
    private static final String RSA_PUBLIC_KEY = "mysecretkey";
    private static final String AUTH0_ISSUER = "https://dev-imfo33vsyswl7flj.us.auth0.com/";
    private static final long ACCESS_EXPIRATION_TIME_MS = 1 * 60 * 1000; // 1 min
    private static final long REFRESH_EXPIRATION_TIME_MS = 20 * 60 * 1000; // 20 min

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
    public static String createRefreshToken(int userId, Role userRole) {
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

    public static String createAccessToken(int userId, Role userRole) {
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
    public static JSONObject getTokenParams(String token) {
        try {


            Claims claims = Jwts.parser()
                    .setSigningKey(SIGNING_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            if (claims == null) {
                return null;
            }
            Integer userId = claims.get("userId", Integer.class);
            String userRole = claims.get("userRole", String.class);
            if (userId == null || userRole == null) {
                return null;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", userId);
            jsonObject.put("role", userRole);


            return jsonObject;
        }catch (Exception e){
            log.error("Could not get local token params. Exception: " + e);
            return null;
        }
    }

    public static JSONObject getAuth0TokenPrams(String token){
        JSONObject jsonObject = new JSONObject();
        DecodedJWT decodedJWT = JWT.decode(token);
        String kid = decodedJWT.getKeyId();
        System.out.println(new Date(System.currentTimeMillis()));
        Algorithm algorithm = Algorithm.RSA256(getPublicKey(kid), null);
        try {
            JWT.require(algorithm)
                    .withIssuer(AUTH0_ISSUER)
                    .withAudience("https://dev-imfo33vsyswl7flj.us.auth0.com/userinfo")
                    .build()
                    .verify(token);

            int id = (int) getUserId(decodedJWT.getSubject());

            List<String> roles = decodedJWT.getClaim("user-roles").asList(String.class);
            String email = decodedJWT.getClaim("user-email").asString();
            System.out.println("Token is valid! id: " + id +", email: " + email + ", roles: " + roles);
            jsonObject.put("id", id);
            jsonObject.put("role", roles.get(0));
            jsonObject.put("email", email);

            log.info("Successfully got auth0 token params");

            return jsonObject;
        }catch (Exception e){
            log.error("Could not get auth0 token params");
            return null;
        }

    }

    private static RSAPublicKey getPublicKey(String kid) {
        try {
            JwkProvider jwkProvider = new JwkProviderBuilder(AUTH0_ISSUER).build();
            Jwk jwk = jwkProvider.get(kid);
            return (RSAPublicKey) jwk.getPublicKey();
        } catch (Exception e) {
            log.error("Public key: " + e);
            return null;
        }
    }

    private static long getUserId(String user_id) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(user_id.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            return number.longValue();
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

}
