package unbosque.edu.co.Spoticlone.service;

import org.springframework.stereotype.Service;
import unbosque.edu.co.Spoticlone.dto.response.CancionResponse;
import unbosque.edu.co.Spoticlone.repository.pg.LikeRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UsuarioService usuarioService;

    public LikeService(LikeRepository likeRepository, UsuarioService usuarioService) {
        this.likeRepository = likeRepository;
        this.usuarioService = usuarioService;
    }

    public void darLike(int idCancion, String email) {
        int idUsuario = usuarioService.findIdByEmail(email);
        likeRepository.darLike(idUsuario, idCancion);
    }

    public void quitarLike(int idCancion, String email) {
        int idUsuario = usuarioService.findIdByEmail(email);
        likeRepository.quitarLike(idUsuario, idCancion);
    }

    public List<CancionResponse> getFavoritos(int idUsuario) {
        return likeRepository.findFavoritosByUsuario(idUsuario);
    }
}
