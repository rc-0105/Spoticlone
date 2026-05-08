package unbosque.edu.co.Spoticlone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Security configuration — exposes BCryptPasswordEncoder as a Spring bean.
 * Single source of truth for encoding strength (default strength = 10).
 */
@Configuration
public class SecurityConfig {

    /**
     * BCrypt encoder bean — injected wherever password hashing is needed.
     * Strength of 10 provides good security vs speed tradeoff for this app.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}