package unbosque.edu.co.Spoticlone.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unbosque.edu.co.Spoticlone.dto.request.RegistrarReproduccionRequest;
import unbosque.edu.co.Spoticlone.dto.response.ApiResponse;
import unbosque.edu.co.Spoticlone.dto.response.GeneroFavoritoResponse;
import unbosque.edu.co.Spoticlone.dto.response.ReproduccionResponse;
import unbosque.edu.co.Spoticlone.dto.response.TopCancionResponse;
import unbosque.edu.co.Spoticlone.exception.BusinessException;
import unbosque.edu.co.Spoticlone.service.ReproduccionService;
import unbosque.edu.co.Spoticlone.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/reproducciones")
public class ReproduccionController {

    private final ReproduccionService reproduccionService;
    private final UsuarioService usuarioService;

    public ReproduccionController(ReproduccionService reproduccionService, UsuarioService usuarioService) {
        this.reproduccionService = reproduccionService;
        this.usuarioService = usuarioService;
    }

    /** POST /api/reproducciones */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registrar(
            HttpServletRequest request,
            @Valid @RequestBody RegistrarReproduccionRequest req) {
        String email = (String) request.getAttribute("userEmail");
        int idUsuario = usuarioService.findIdByEmail(email);
        reproduccionService.registrar(idUsuario, req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Reproducción registrada"));
    }

    /** GET /api/reproducciones/historial/{id_usuario} */
    @GetMapping("/historial/{idUsuario}")
    public ResponseEntity<ApiResponse<List<ReproduccionResponse>>> historial(
            HttpServletRequest request,
            @PathVariable int idUsuario) {
        requireOwnership(request, idUsuario);
        List<ReproduccionResponse> historial = reproduccionService.historialByUsuario(idUsuario);
        return ResponseEntity.ok(ApiResponse.ok(historial));
    }

    /** GET /api/reproducciones/top */
    @GetMapping("/top")
    public ResponseEntity<ApiResponse<List<TopCancionResponse>>> top10() {
        List<TopCancionResponse> top = reproduccionService.top10Canciones();
        return ResponseEntity.ok(ApiResponse.ok("OK", top));
    }

    /** GET /api/reproducciones/genero-favorito/{id_usuario} */
    @GetMapping("/genero-favorito/{idUsuario}")
    public ResponseEntity<ApiResponse<List<GeneroFavoritoResponse>>> generoFavorito(
            HttpServletRequest request,
            @PathVariable int idUsuario) {
        requireOwnership(request, idUsuario);
        List<GeneroFavoritoResponse> genero = reproduccionService.generoFavorito(idUsuario);
        return ResponseEntity.ok(ApiResponse.ok(genero));
    }

    private void requireOwnership(HttpServletRequest request, int idUsuario) {
        String email = (String) request.getAttribute("userEmail");
        int authId = usuarioService.findIdByEmail(email);
        if (authId != idUsuario) {
            throw new BusinessException("Acceso denegado", 403);
        }
    }
}
