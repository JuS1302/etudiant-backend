package com.openclassrooms.etudiant.service;

import com.openclassrooms.etudiant.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests unitaires du JwtService : on fabrique un token puis on vérifie qu'on sait le relire.
 */
public class JwtServiceTest {
    private static final String LOGIN = "LOGIN";
    // Clé de test d'au moins 32 caractères (exigé par l'algorithme HS256)
    private static final String SECRET = "cle-secrete-de-test-de-32-caracteres-min";
    private static final long ONE_HOUR = 3600000L;

    private JwtService jwtService;
    private User user;

    /**
     * Avant chaque test : crée le service avec une clé et une durée de test,
     * à la place des valeurs normalement lues dans le fichier .env.
     */
    @BeforeEach
    public void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", ONE_HOUR);
        user = new User();
        user.setLogin(LOGIN);
    }

    /**
     * Le token généré contient le login de l'utilisateur.
     */
    @Test
    public void test_generated_token_contains_login() {
        // WHEN
        String token = jwtService.generateToken(user);

        // THEN : on relit le login inscrit dans le token
        assertThat(jwtService.extractUsername(token)).isEqualTo(LOGIN);
    }

    /**
     * Un token généré pour un utilisateur est valide pour ce même utilisateur.
     */
    @Test
    public void test_token_is_valid_for_its_user() {
        // GIVEN
        String token = jwtService.generateToken(user);

        // WHEN / THEN
        assertThat(jwtService.isTokenValid(token, user)).isTrue();
    }
}
