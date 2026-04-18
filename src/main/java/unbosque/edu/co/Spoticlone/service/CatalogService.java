package unbosque.edu.co.Spoticlone.service;

import org.springframework.stereotype.Service;
import unbosque.edu.co.Spoticlone.dto.response.AlbumResponse;
import unbosque.edu.co.Spoticlone.dto.response.ArtistaResponse;
import unbosque.edu.co.Spoticlone.dto.response.CancionResponse;
import unbosque.edu.co.Spoticlone.dto.response.GeneroResponse;
import unbosque.edu.co.Spoticlone.repository.pg.CatalogRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CatalogService {

    private final CatalogRepository catalogRepository;

    public CatalogService(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    public List<CancionResponse> findCanciones(String genero, String artista) {
        return catalogRepository.findCanciones(genero, artista);
    }

    public List<CancionResponse> findCancionesByAlbum(int idAlbum) {
        return catalogRepository.findCancionesByAlbum(idAlbum);
    }

    public List<ArtistaResponse> findArtistas() {
        return catalogRepository.findArtistas();
    }

    public ArtistaResponse findArtistaById(int idArtista) {
        return catalogRepository.findArtistaById(idArtista)
                .orElseThrow(() -> new NoSuchElementException(
                        "Artista con id " + idArtista + " no encontrado"));
    }

    public List<GeneroResponse> findGeneros() {
        return catalogRepository.findGeneros();
    }

    public List<AlbumResponse> findAlbumes() {
        return catalogRepository.findAlbumes();
    }
}
