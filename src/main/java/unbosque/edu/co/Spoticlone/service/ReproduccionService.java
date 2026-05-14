package unbosque.edu.co.Spoticlone.service;

import org.springframework.stereotype.Service;
import unbosque.edu.co.Spoticlone.dto.request.RegistrarReproduccionRequest;
import unbosque.edu.co.Spoticlone.dto.response.CancionResponse;
import unbosque.edu.co.Spoticlone.dto.response.GeneroFavoritoResponse;
import unbosque.edu.co.Spoticlone.dto.response.ReproduccionResponse;
import unbosque.edu.co.Spoticlone.dto.response.TopCancionResponse;
import unbosque.edu.co.Spoticlone.repository.mongo.ReproduccionRepository;
import unbosque.edu.co.Spoticlone.repository.pg.CatalogRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReproduccionService {

    private final ReproduccionRepository reproduccionRepository;
    private final CatalogRepository catalogRepository;

    public ReproduccionService(ReproduccionRepository reproduccionRepository,
                               CatalogRepository catalogRepository) {
        this.reproduccionRepository = reproduccionRepository;
        this.catalogRepository = catalogRepository;
    }

    public void registrar(int idUsuario, RegistrarReproduccionRequest req) {
        CancionResponse cancion = catalogRepository.findCancionById(req.getIdCancion())
                .orElseThrow(() -> new NoSuchElementException(
                        "Canción con id " + req.getIdCancion() + " no encontrada"));
        reproduccionRepository.registrar(idUsuario, req, cancion);
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
