package unbosque.edu.co.Spoticlone.service;

import org.springframework.stereotype.Service;
import unbosque.edu.co.Spoticlone.dto.response.SuscripcionResponse;
import unbosque.edu.co.Spoticlone.repository.pg.SuscripcionRepository;

import java.util.NoSuchElementException;

@Service
public class SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;

    public SuscripcionService(SuscripcionRepository suscripcionRepository) {
        this.suscripcionRepository = suscripcionRepository;
    }

    public void cambiarSuscripcion(int idUsuario, String nuevoTipo) {
        suscripcionRepository.cambiarSuscripcion(idUsuario, nuevoTipo);
    }

    public SuscripcionResponse findByUsuario(int idUsuario) {
        return suscripcionRepository.findByUsuario(idUsuario)
                .orElseThrow(() -> new NoSuchElementException(
                        "Suscripción para usuario " + idUsuario + " no encontrada"));
    }
}
