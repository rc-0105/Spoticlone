package unbosque.edu.co.Spoticlone.service;

import org.springframework.stereotype.Service;
import unbosque.edu.co.Spoticlone.dto.response.UsuarioResponse;
import unbosque.edu.co.Spoticlone.repository.pg.UsuarioRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponse findById(int idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new NoSuchElementException(
                        "Usuario con id " + idUsuario + " no encontrado"));
    }

    public List<UsuarioResponse> findAll() {
        return usuarioRepository.findAll();
    }

    public int findIdByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(UsuarioResponse::getIdUsuario)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
    }

    public String findRolByEmail(String email) {
        return usuarioRepository.findRolByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
    }
}
