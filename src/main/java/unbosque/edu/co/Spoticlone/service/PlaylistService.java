package unbosque.edu.co.Spoticlone.service;

import org.springframework.stereotype.Service;
import unbosque.edu.co.Spoticlone.dto.response.PlaylistDetalleResponse;
import unbosque.edu.co.Spoticlone.dto.response.PlaylistResponse;
import unbosque.edu.co.Spoticlone.repository.pg.PlaylistRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;

    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public void crearPlaylist(int idUsuario, String nombre, String descripcion, boolean esPublica) {
        playlistRepository.crearPlaylist(idUsuario, nombre, descripcion, esPublica);
    }

    public PlaylistDetalleResponse findById(int idPlaylist) {
        PlaylistDetalleResponse pd = playlistRepository.findById(idPlaylist)
                .orElseThrow(() -> new NoSuchElementException(
                        "Playlist con id " + idPlaylist + " no encontrada"));
        pd.setCanciones(playlistRepository.findCancionesDePlaylist(idPlaylist));
        pd.setDuracionTotalSeg(playlistRepository.duracionTotal(idPlaylist));
        return pd;
    }

    public List<PlaylistResponse> findByUsuario(int idUsuario) {
        return playlistRepository.findByUsuario(idUsuario);
    }

    public void agregarCancion(int idPlaylist, int idCancion) {
        playlistRepository.agregarCancion(idPlaylist, idCancion);
    }

    public List<PlaylistResponse> findPublicas() {
        return playlistRepository.findPublicas();
    }
}
