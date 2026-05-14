package unbosque.edu.co.Spoticlone.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import unbosque.edu.co.Spoticlone.dto.response.UsuarioResponse;
import unbosque.edu.co.Spoticlone.exception.BusinessException;
import unbosque.edu.co.Spoticlone.repository.pg.LoginData;
import unbosque.edu.co.Spoticlone.repository.pg.UsuarioRepository;

import java.util.NoSuchElementException;

/**
 * Authentication service — handles login and registration with BCrypt + JWT.
 * Passwords are never stored in plain text; only the BCrypt hash goes to the DB.
 */
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(
            UsuarioRepository usuarioRepository,
            JwtService jwtService,
            BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Authenticates a user by email and password, returning a JWT on success.
     * Throws BusinessException on invalid credentials.
     */
    public String login(String email, String plainPassword) {
        LoginData data = usuarioRepository.findForLogin(email)
                .orElseThrow(() -> new BusinessException("Credenciales inválidas", 401));

        if (!passwordEncoder.matches(plainPassword, data.getPasswordHash())) {
            throw new BusinessException("Credenciales inválidas", 401);
        }

        return jwtService.generateToken(data.getIdUsuario(), email, data.getNombre(), data.getRol());
    }

    /**
     * Registers a new user with a BCrypt-hashed password.
     * The stored procedure expects a pre-hashed password, so we hash it here.
     */
    public void register(String nombre, String email, String plainPassword) {
        String hash = passwordEncoder.encode(plainPassword);
        usuarioRepository.registrar(nombre, email, hash);
    }

    /**
     * Returns user info for a given email (used after JWT validation).
     */
    public UsuarioResponse findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(
                        "Usuario con email " + email + " no encontrado"));
    }
}