package unbosque.edu.co.Spoticlone.dto.response;

import java.util.List;

public class AlbumDetalleResponse extends AlbumResponse {

    private List<CancionResponse> canciones;

    public List<CancionResponse> getCanciones() { return canciones; }
    public void setCanciones(List<CancionResponse> canciones) { this.canciones = canciones; }
}
