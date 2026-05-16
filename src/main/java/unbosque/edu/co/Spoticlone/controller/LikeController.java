package unbosque.edu.co.Spoticlone.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unbosque.edu.co.Spoticlone.dto.response.ApiResponse;
import unbosque.edu.co.Spoticlone.dto.response.CancionResponse;
import unbosque.edu.co.Spoticlone.service.LikeService;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    /** POST /api/likes/{idCancion} */
    @PostMapping("/{idCancion}")
    public ResponseEntity<ApiResponse<Void>> darLike(
            @PathVariable int idCancion,
            HttpServletRequest request) {
        String email = (String) request.getAttribute("userEmail");
        likeService.darLike(idCancion, email);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Like registrado exitosamente"));
    }

    /** DELETE /api/likes/{idCancion} */
    @DeleteMapping("/{idCancion}")
    public ResponseEntity<ApiResponse<Void>> quitarLike(
            @PathVariable int idCancion,
            HttpServletRequest request) {
        String email = (String) request.getAttribute("userEmail");
        likeService.quitarLike(idCancion, email);
        return ResponseEntity.ok(ApiResponse.ok("Like eliminado exitosamente", null));
    }

    /** GET /api/likes/usuario/{idUsuario} */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<ApiResponse<List<CancionResponse>>> getFavoritos(
            @PathVariable int idUsuario) {
        List<CancionResponse> favoritos = likeService.getFavoritos(idUsuario);
        return ResponseEntity.ok(ApiResponse.ok(favoritos));
    }
}
