package unbosque.edu.co.Spoticlone.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unbosque.edu.co.Spoticlone.dto.response.ApiResponse;
import unbosque.edu.co.Spoticlone.dto.response.UsuarioResponse;
import unbosque.edu.co.Spoticlone.exception.BusinessException;
import unbosque.edu.co.Spoticlone.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /** GET /api/usuarios/me */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UsuarioResponse>> getMe(HttpServletRequest request) {
        String email = (String) request.getAttribute("userEmail");
        return ResponseEntity.ok(ApiResponse.ok("Usuario obtenido", usuarioService.findByEmail(email)));
    }

    /** GET /api/usuarios/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponse>> findById(@PathVariable int id) {
        UsuarioResponse usuario = usuarioService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok(usuario));
    }

    /** GET /api/usuarios */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioResponse>>> findAll(HttpServletRequest request) {
        String email = (String) request.getAttribute("userEmail");
        if (!"admin".equals(usuarioService.findRolByEmail(email))) {
            throw new BusinessException("Acceso denegado", 403);
        }
        List<UsuarioResponse> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(ApiResponse.ok(usuarios));
    }
}
