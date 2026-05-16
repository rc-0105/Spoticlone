package unbosque.edu.co.Spoticlone.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import unbosque.edu.co.Spoticlone.dto.response.ApiResponse;
import unbosque.edu.co.Spoticlone.service.JwtService;

import java.io.IOException;

/**
 * JWT authentication filter — intercepts all /api/* requests except /api/auth/*.
 * Validates the Bearer token and sets user identity as a request attribute.
 * No Spring Security filter chain; uses plain servlet filter approach.
 *
 * Registration: FilterConfig (FilterRegistrationBean) — runs before DispatcherServlet.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    public static final String USER_EMAIL_ATTRIBUTE = "userEmail";

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(JwtService jwtService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Allow CORS preflight and /api/auth/* without authentication
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            sendUnauthorized(response, "Token de autenticación requerido");
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        if (!jwtService.validateToken(token)) {
            sendUnauthorized(response, "Token inválido o expirado");
            return;
        }

        String email = jwtService.getEmailFromToken(token);

        if (!jwtService.isUserActive(email)) {
            sendUnauthorized(response, "Usuario inactivo o no encontrado");
            return;
        }

        request.setAttribute(USER_EMAIL_ATTRIBUTE, email);

        filterChain.doFilter(request, response);
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), ApiResponse.error(message));
    }
}