package unbosque.edu.co.Spoticlone.service;

import org.springframework.stereotype.Service;
import unbosque.edu.co.Spoticlone.dto.response.PlaylistDetalleResponse;
import unbosque.edu.co.Spoticlone.dto.response.PlaylistResponse;
import unbosque.edu.co.Spoticlone.exception.BusinessException;
import unbosque.edu.co.Spoticlone.repository.pg.PlaylistRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UsuarioService usuarioService;

    public PlaylistService(PlaylistRepository playlistRepository, UsuarioService usuarioService) {
        this.playlistRepository = playlistRepository;
        this.usuarioService = usuarioService;
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

    public void agregarCancion(int idPlaylist, int idCancion, String email) {
        verificarOwnership(idPlaylist, email);
        playlistRepository.agregarCancion(idPlaylist, idCancion);
    }

    public void eliminarCancion(Long idPlaylist, Long idCancion, String email) {
        verificarOwnership(idPlaylist.intValue(), email);
        playlistRepository.eliminarCancion(idPlaylist.intValue(), idCancion.intValue());
    }

    public void eliminarPlaylist(int idPlaylist, String email) {
        verificarOwnership(idPlaylist, email);
        playlistRepository.eliminarPlaylist(idPlaylist);
    }

    public PlaylistDetalleResponse actualizarPlaylist(int idPlaylist, String email,
                                                      String nombre, String descripcion, boolean esPublica) {
        verificarOwnership(idPlaylist, email);
        return playlistRepository.actualizarPlaylist(idPlaylist, nombre, descripcion, esPublica);
    }

    public List<PlaylistResponse> findPublicas() {
        return playlistRepository.findPublicas();
    }

    private void verificarOwnership(int idPlaylist, String email) {
        int idUsuarioAuth = usuarioService.findIdByEmail(email);
        int idDueno = playlistRepository.findIdUsuarioByPlaylist(idPlaylist)
                .orElseThrow(() -> new NoSuchElementException(
                        "Playlist con id " + idPlaylist + " no encontrada"));
        if (idDueno != idUsuarioAuth) {
            throw new BusinessException("No tenés permiso para modificar esta playlist", 403);
        }
    }
}
