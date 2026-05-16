package unbosque.edu.co.Spoticlone.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * JWT service — handles token generation and validation.
 * No Spring Security filter chain; used by JwtAuthenticationFilter and AuthService.
 */
@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationMs;
    private final JdbcTemplate jdbc;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMs,
            JdbcTemplate jdbcTemplate) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
        this.jdbc = jdbcTemplate;
    }

    /**
     * Generates a JWT token with user claims.
     * Token includes: subject(userId), email, username, role.
     */
    public String generateToken(int userId, String email, String username, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Validates the token: checks signature and expiry.
     * @return true if valid, false if expired or malformed
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extracts the email claim from a validated token.
     * MUST be called only after validateToken() returns true.
     */
    public String getEmailFromToken(String token) {
        return parseClaims(token).get("email", String.class);
    }

    /**
     * Verifica que el usuario exista en BD y tenga activo = true.
     * Se ejecuta en cada request autenticado. Retorna false ante cualquier error.
     */
    public boolean isUserActive(String email) {
        try {
            List<Boolean> result = jdbc.query(
                    "SELECT activo FROM usuario WHERE email = ?",
                    (rs, rowNum) -> rs.getBoolean("activo"),
                    email);
            return !result.isEmpty() && result.get(0);
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}