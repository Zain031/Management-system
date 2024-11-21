package com.abpgroup.managementsystem.security;

import com.abpgroup.managementsystem.model.entity.AppUser;
import com.abpgroup.managementsystem.utils.exeptions.AuthenticationExeption;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtils {
    @Value("${app.managementsystem.jwt.secret-key}")
    private String jwtSecret;
    @Value("${app.managementsystem.jwt.app-name}")
    private String appName;
    @Value("${app.managementsystem.jwt.expire-time}")
    private long jwtExpired;

    private Algorithm algorithm;

    @PostConstruct
    private void initAlgorithm() {
        this.algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(AppUser appUser) {
        return JWT.create()
                .withIssuer(appName)
                .withSubject(String.valueOf(appUser.getId()))
                .withExpiresAt(Instant.now().plusSeconds(jwtExpired))
                .withIssuedAt(Instant.now())
                .withClaim("role", appUser.getRole().name())
                .sign(algorithm);
    }

    public boolean verifyJwtToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getIssuer().equals(appName);
        }catch (JWTVerificationException e) {
            throw new AuthenticationExeption("Invalid token");
        }
    }

    public Map<String, String> getUserInfoByToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("idUser", decodedJWT.getSubject());
        userInfo.put("role", decodedJWT.getClaim("role").asString());
        return userInfo;
    }
}
