package com.openclassrooms.etudiant.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${JWT_SECRET}")
    private String secretKey;

    @Value("${JWT_EXPIRATION}")
    private long jwtExpiration;

    /**
     * Génère un token JWT signé contenant le login de l'utilisateur.
     */
    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Lit le login (le "subject") inscrit dans le token.
     * Lève une JwtException si le token est faux, falsifié ou expiré.
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Vérifie que le token appartient bien à cet utilisateur.
     * (La signature et l'expiration sont déjà vérifiées par extractUsername.)
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername());
    }

    /**
     * Transforme la clé secrète du .env en clé utilisable pour signer le token.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
