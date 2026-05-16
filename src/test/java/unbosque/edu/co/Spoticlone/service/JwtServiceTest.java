package unbosque.edu.co.Spoticlone.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for JwtService.
 * Tests token generation, validation, and email extraction.
 * JdbcTemplate is mocked — isUserActive() is not exercised here.
 */
class JwtServiceTest {

    // 32+ chars for HMAC-SHA256
    private static final String TEST_SECRET = "Spoticlone2026TestSecretKeyQueTieneAlMenos256Bits";
    private static final long TEST_EXPIRATION = 86400000L; // 1 day in ms

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(TEST_SECRET, TEST_EXPIRATION, mock(JdbcTemplate.class));
    }

    @Test
    void generateToken_shouldCreateValidToken() {
        String email = "usuario@test.com";
        String token = jwtService.generateToken(1, email, "testuser", "user");
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void validateToken_shouldReturnTrue_forValidToken() {
        String token = jwtService.generateToken(1, "test@example.com", "testuser", "user");
        assertTrue(jwtService.validateToken(token));
    }

    @Test
    void validateToken_shouldReturnFalse_forInvalidToken() {
        assertFalse(jwtService.validateToken("invalid.token.here"));
    }

    @Test
    void validateToken_shouldReturnFalse_forNullToken() {
        assertFalse(jwtService.validateToken(null));
    }

    @Test
    void validateToken_shouldReturnFalse_forEmptyToken() {
        assertFalse(jwtService.validateToken(""));
    }

    @Test
    void getEmailFromToken_shouldExtractCorrectEmail() {
        String email = "john.doe@example.com";
        String token = jwtService.generateToken(1, email, "testuser", "user");
        assertEquals(email, jwtService.getEmailFromToken(token));
    }

    @Test
    void getEmailFromToken_shouldBeConsistent_acrossMultipleGenerations() {
        String email = "maria@test.co";
        // Generate multiple tokens — email must be extractable from each
        for (int i = 0; i < 5; i++) {
            String token = jwtService.generateToken(1, email, "testuser", "user");
            assertEquals(email, jwtService.getEmailFromToken(token));
        }
    }

    @Test
    void differentSecrets_shouldProduceDifferentValidation() {
        JwtService other = new JwtService(
                "DiferenteSecretKeyQueTieneAlMenos256BitsDeLargo",
                TEST_EXPIRATION,
                mock(JdbcTemplate.class));
        String token = jwtService.generateToken(1, "user@test.com", "testuser", "user");
        assertTrue(jwtService.validateToken(token));
        assertFalse(other.validateToken(token));
    }
}