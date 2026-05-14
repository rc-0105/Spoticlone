package unbosque.edu.co.Spoticlone.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unbosque.edu.co.Spoticlone.dto.request.RegistrarUsuarioRequest;
import unbosque.edu.co.Spoticlone.dto.response.ApiResponse;
import unbosque.edu.co.Spoticlone.dto.response.UsuarioResponse;
import unbosque.edu.co.Spoticlone.exception.BusinessException;
import unbosque.edu.co.Spoticlone.service.UsuarioService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /** POST /api/usuarios/registrar */
    @PostMapping("/registrar")
    public ResponseEntity<ApiResponse<Map<String, String>>> registrar(
            @Valid @RequestBody RegistrarUsuarioRequest req) {
        usuarioService.registrar(req.getNombre(), req.getEmail(), req.getPassword());
        Map<String, String> data = Map.of("email", req.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Usuario registrado exitosamente", data));
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
