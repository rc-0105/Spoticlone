package unbosque.edu.co.Spoticlone.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unbosque.edu.co.Spoticlone.dto.request.ActualizarPlaylistRequest;
import unbosque.edu.co.Spoticlone.dto.request.AgregarCancionPlaylistRequest;
import unbosque.edu.co.Spoticlone.dto.request.CrearPlaylistRequest;
import unbosque.edu.co.Spoticlone.dto.response.ApiResponse;
import unbosque.edu.co.Spoticlone.dto.response.PlaylistDetalleResponse;
import unbosque.edu.co.Spoticlone.dto.response.PlaylistResponse;
import unbosque.edu.co.Spoticlone.service.PlaylistService;
import unbosque.edu.co.Spoticlone.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final UsuarioService usuarioService;

    public PlaylistController(PlaylistService playlistService, UsuarioService usuarioService) {
        this.playlistService = playlistService;
        this.usuarioService = usuarioService;
    }

    /** POST /api/playlists */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> crearPlaylist(
            HttpServletRequest request,
            @Valid @RequestBody CrearPlaylistRequest req) {
        String email = (String) request.getAttribute("userEmail");
        int idUsuario = usuarioService.findIdByEmail(email);
        playlistService.crearPlaylist(
                idUsuario,
                req.getNombre(),
                req.getDescripcion(),
                Boolean.TRUE.equals(req.getEsPublica())
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Playlist creada exitosamente"));
    }

    /** GET /api/playlists/publicas */
    @GetMapping("/publicas")
    public ResponseEntity<ApiResponse<List<PlaylistResponse>>> findPublicas() {
        List<PlaylistResponse> playlists = playlistService.findPublicas();
        return ResponseEntity.ok(ApiResponse.ok(playlists));
    }

    /** GET /api/playlists/usuario/{id_usuario} */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<ApiResponse<List<PlaylistResponse>>> findByUsuario(
            @PathVariable int idUsuario) {
        List<PlaylistResponse> playlists = playlistService.findByUsuario(idUsuario);
        return ResponseEntity.ok(ApiResponse.ok(playlists));
    }

    /** GET /api/playlists/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlaylistDetalleResponse>> findById(@PathVariable int id) {
        PlaylistDetalleResponse playlist = playlistService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok(playlist));
    }

    /** POST /api/playlists/{id}/canciones */
    @PostMapping("/{id}/canciones")
    public ResponseEntity<ApiResponse<Void>> agregarCancion(
            @PathVariable int id,
            @Valid @RequestBody AgregarCancionPlaylistRequest req,
            HttpServletRequest request) {
        String email = (String) request.getAttribute("userEmail");
        playlistService.agregarCancion(id, req.getIdCancion(), email);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Canción agregada exitosamente"));
    }

    /** DELETE /api/playlists/{id}/canciones/{idCancion} */
    @DeleteMapping("/{id}/canciones/{idCancion}")
    public ResponseEntity<ApiResponse<Void>> eliminarCancion(
            @PathVariable Long id,
            @PathVariable Long idCancion,
            HttpServletRequest request) {
        String email = (String) request.getAttribute("userEmail");
        playlistService.eliminarCancion(id, idCancion, email);
        return ResponseEntity.ok(ApiResponse.ok("Canción eliminada exitosamente", null));
    }

    /** DELETE /api/playlists/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarPlaylist(
            @PathVariable int id,
            HttpServletRequest request) {
        String email = (String) request.getAttribute("userEmail");
        playlistService.eliminarPlaylist(id, email);
        return ResponseEntity.ok(ApiResponse.ok("Playlist eliminada exitosamente", null));
    }

    /** PUT /api/playlists/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PlaylistDetalleResponse>> actualizarPlaylist(
            @PathVariable int id,
            @Valid @RequestBody ActualizarPlaylistRequest req,
            HttpServletRequest request) {
        String email = (String) request.getAttribute("userEmail");
        PlaylistDetalleResponse updated = playlistService.actualizarPlaylist(
                id, email, req.getNombre(), req.getDescripcion(), req.isEsPublica()
        );
        return ResponseEntity.ok(ApiResponse.ok("Playlist actualizada exitosamente", updated));
    }
}
