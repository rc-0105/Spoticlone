package unbosque.edu.co.Spoticlone.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unbosque.edu.co.Spoticlone.dto.request.CambiarSuscripcionRequest;
import unbosque.edu.co.Spoticlone.dto.response.ApiResponse;
import unbosque.edu.co.Spoticlone.dto.response.SuscripcionResponse;
import unbosque.edu.co.Spoticlone.service.SuscripcionService;

@RestController
@RequestMapping("/api/suscripciones")
public class SuscripcionController {

    private final SuscripcionService suscripcionService;

    public SuscripcionController(SuscripcionService suscripcionService) {
        this.suscripcionService = suscripcionService;
    }

    /** PUT /api/suscripciones/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cambiarSuscripcion(
            @PathVariable int id,
            @Valid @RequestBody CambiarSuscripcionRequest req) {
        suscripcionService.cambiarSuscripcion(id, req.getNuevoTipo());
        return ResponseEntity.ok(ApiResponse.ok("Suscripción actualizada exitosamente", null));
    }

    /** GET /api/suscripciones/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SuscripcionResponse>> findByUsuario(@PathVariable int id) {
        SuscripcionResponse suscripcion = suscripcionService.findByUsuario(id);
        return ResponseEntity.ok(ApiResponse.ok(suscripcion));
    }
}
