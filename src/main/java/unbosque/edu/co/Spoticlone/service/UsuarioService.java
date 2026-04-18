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

    public void registrar(String nombre, String email, String passwordHash) {
        usuarioRepository.registrar(nombre, email, passwordHash);
    }

    public UsuarioResponse findById(int idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new NoSuchElementException(
                        "Usuario con id " + idUsuario + " no encontrado"));
    }

    public List<UsuarioResponse> findAll() {
        return usuarioRepository.findAll();
    }
}
