package unbosque.edu.co.Spoticlone.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unbosque.edu.co.Spoticlone.dto.request.AgregarCancionPlaylistRequest;
import unbosque.edu.co.Spoticlone.dto.request.CrearPlaylistRequest;
import unbosque.edu.co.Spoticlone.dto.response.ApiResponse;
import unbosque.edu.co.Spoticlone.dto.response.PlaylistDetalleResponse;
import unbosque.edu.co.Spoticlone.dto.response.PlaylistResponse;
import unbosque.edu.co.Spoticlone.service.PlaylistService;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    /** POST /api/playlists */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> crearPlaylist(
            @Valid @RequestBody CrearPlaylistRequest req) {
        playlistService.crearPlaylist(
                req.getIdUsuario(),
                req.getNombre(),
                req.getDescripcion(),
                Boolean.TRUE.equals(req.getEsPublica())
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Playlist creada exitosamente"));
    }

    /** GET /api/playlists/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlaylistDetalleResponse>> findById(@PathVariable int id) {
        PlaylistDetalleResponse playlist = playlistService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok(playlist));
    }

    /** GET /api/playlists/usuario/{id_usuario} */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<ApiResponse<List<PlaylistResponse>>> findByUsuario(
            @PathVariable int idUsuario) {
        List<PlaylistResponse> playlists = playlistService.findByUsuario(idUsuario);
        return ResponseEntity.ok(ApiResponse.ok(playlists));
    }

    /** POST /api/playlists/{id}/canciones */
    @PostMapping("/{id}/canciones")
    public ResponseEntity<ApiResponse<Void>> agregarCancion(
            @PathVariable int id,
            @Valid @RequestBody AgregarCancionPlaylistRequest req) {
        playlistService.agregarCancion(id, req.getIdCancion());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Canción agregada exitosamente"));
    }

    /** GET /api/playlists/publicas */
    @GetMapping("/publicas")
    public ResponseEntity<ApiResponse<List<PlaylistResponse>>> findPublicas() {
        List<PlaylistResponse> playlists = playlistService.findPublicas();
        return ResponseEntity.ok(ApiResponse.ok(playlists));
    }
}
