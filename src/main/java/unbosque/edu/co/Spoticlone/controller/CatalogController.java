package unbosque.edu.co.Spoticlone.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unbosque.edu.co.Spoticlone.dto.response.*;
import unbosque.edu.co.Spoticlone.service.CatalogService;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    /** GET /api/catalog/search?q=texto */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CancionResponse>>> search(
            @RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.ok(catalogService.searchCanciones(q)));
    }

    /**
     * GET /api/catalog/canciones
     * GET /api/catalog/canciones?genero=Rock&artista=The%20Cure
     */
    @GetMapping("/canciones")
    public ResponseEntity<ApiResponse<List<CancionResponse>>> findCanciones(
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) String artista) {
        List<CancionResponse> canciones = catalogService.findCanciones(genero, artista);
        return ResponseEntity.ok(ApiResponse.ok(canciones));
    }

    /** GET /api/catalog/canciones/{id} */
    @GetMapping("/canciones/{id}")
    public ResponseEntity<ApiResponse<CancionResponse>> getCancionById(@PathVariable int id) {
        return ResponseEntity.ok(ApiResponse.ok(catalogService.findCancionById(id)));
    }

    /** GET /api/catalog/artistas */
    @GetMapping("/artistas")
    public ResponseEntity<ApiResponse<List<ArtistaResponse>>> findArtistas() {
        List<ArtistaResponse> artistas = catalogService.findArtistas();
        return ResponseEntity.ok(ApiResponse.ok(artistas));
    }

    /** GET /api/catalog/artistas/{id} */
    @GetMapping("/artistas/{id}")
    public ResponseEntity<ApiResponse<ArtistaResponse>> findArtistaById(@PathVariable int id) {
        ArtistaResponse artista = catalogService.findArtistaById(id);
        return ResponseEntity.ok(ApiResponse.ok(artista));
    }

    /** GET /api/catalog/generos */
    @GetMapping("/generos")
    public ResponseEntity<ApiResponse<List<GeneroResponse>>> findGeneros() {
        List<GeneroResponse> generos = catalogService.findGeneros();
        return ResponseEntity.ok(ApiResponse.ok(generos));
    }

    /** GET /api/catalog/albumes */
    @GetMapping("/albumes")
    public ResponseEntity<ApiResponse<List<AlbumResponse>>> findAlbumes() {
        List<AlbumResponse> albumes = catalogService.findAlbumes();
        return ResponseEntity.ok(ApiResponse.ok(albumes));
    }

    /** GET /api/catalog/albumes/{id} */
    @GetMapping("/albumes/{id}")
    public ResponseEntity<ApiResponse<AlbumDetalleResponse>> getAlbumById(@PathVariable int id) {
        return ResponseEntity.ok(ApiResponse.ok("Álbum obtenido", catalogService.findAlbumById(id)));
    }

    /** GET /api/catalog/albumes/{id}/canciones */
    @GetMapping("/albumes/{id}/canciones")
    public ResponseEntity<ApiResponse<List<CancionResponse>>> findCancionesByAlbum(
            @PathVariable int id) {
        List<CancionResponse> canciones = catalogService.findCancionesByAlbum(id);
        return ResponseEntity.ok(ApiResponse.ok(canciones));
    }
}
