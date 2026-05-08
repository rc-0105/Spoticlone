package unbosque.edu.co.Spoticlone.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
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

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Allow /api/auth/* endpoints without authentication
        if (path.startsWith("/api/auth")) {
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
        request.setAttribute(USER_EMAIL_ATTRIBUTE, email);

        filterChain.doFilter(request, response);
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(
                "{\"success\":false,\"message\":\"" + message + "\",\"data\":null}"
        );
    }
}