package unbosque.edu.co.Spoticlone.service;

import org.springframework.stereotype.Service;
import unbosque.edu.co.Spoticlone.dto.request.RegistrarReproduccionRequest;
import unbosque.edu.co.Spoticlone.dto.response.GeneroFavoritoResponse;
import unbosque.edu.co.Spoticlone.dto.response.ReproduccionResponse;
import unbosque.edu.co.Spoticlone.dto.response.TopCancionResponse;
import unbosque.edu.co.Spoticlone.repository.mongo.ReproduccionRepository;

import java.util.List;

@Service
public class ReproduccionService {

    private final ReproduccionRepository reproduccionRepository;

    public ReproduccionService(ReproduccionRepository reproduccionRepository) {
        this.reproduccionRepository = reproduccionRepository;
    }

    public void registrar(RegistrarReproduccionRequest req) {
        reproduccionRepository.registrar(req);
    }

    public List<ReproduccionResponse> historialByUsuario(int idUsuario) {
        return reproduccionRepository.historialByUsuario(idUsuario);
    }

    public List<TopCancionResponse> top10Canciones() {
        return reproduccionRepository.top10Canciones();
    }

    public List<GeneroFavoritoResponse> generoFavorito(int idUsuario) {
        return reproduccionRepository.generoFavorito(idUsuario);
    }
}
