package unbosque.edu.co.Spoticlone.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unbosque.edu.co.Spoticlone.dto.request.RegistrarReproduccionRequest;
import unbosque.edu.co.Spoticlone.dto.response.ApiResponse;
import unbosque.edu.co.Spoticlone.dto.response.GeneroFavoritoResponse;
import unbosque.edu.co.Spoticlone.dto.response.ReproduccionResponse;
import unbosque.edu.co.Spoticlone.dto.response.TopCancionResponse;
import unbosque.edu.co.Spoticlone.service.ReproduccionService;

import java.util.List;

@RestController
@RequestMapping("/api/reproducciones")
public class ReproduccionController {

    private final ReproduccionService reproduccionService;

    public ReproduccionController(ReproduccionService reproduccionService) {
        this.reproduccionService = reproduccionService;
    }

    /** POST /api/reproducciones */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registrar(
            @Valid @RequestBody RegistrarReproduccionRequest req) {
        reproduccionService.registrar(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Reproducción registrada"));
    }

    /** GET /api/reproducciones/historial/{id_usuario} */
    @GetMapping("/historial/{idUsuario}")
    public ResponseEntity<ApiResponse<List<ReproduccionResponse>>> historial(
            @PathVariable int idUsuario) {
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
            @PathVariable int idUsuario) {
        List<GeneroFavoritoResponse> genero = reproduccionService.generoFavorito(idUsuario);
        return ResponseEntity.ok(ApiResponse.ok(genero));
    }
}
