package unbosque.edu.co.Spoticlone.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unbosque.edu.co.Spoticlone.dto.request.LoginRequest;
import unbosque.edu.co.Spoticlone.dto.request.RegistrarUsuarioRequest;
import unbosque.edu.co.Spoticlone.dto.response.ApiResponse;
import unbosque.edu.co.Spoticlone.service.AuthService;

import java.util.Map;

/**
 * Authentication controller — exposes /api/auth/login and /api/auth/register.
 * All other /api/* endpoints are protected by JwtAuthenticationFilter.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/login
     * Authenticates user and returns a JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(
            @Valid @RequestBody LoginRequest req) {
        String token = authService.login(req.getEmail(), req.getPassword());
        Map<String, String> data = Map.of("token", token);
        return ResponseEntity.ok(ApiResponse.ok("Login exitoso", data));
    }

    /**
     * POST /api/auth/register
     * Registers a new user with plain password (hashed server-side via BCrypt).
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Map<String, String>>> register(
            @Valid @RequestBody RegistrarUsuarioRequest req) {
        authService.register(req.getNombre(), req.getEmail(), req.getPassword());
        Map<String, String> data = Map.of("email", req.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Usuario registrado exitosamente", data));
    }
}