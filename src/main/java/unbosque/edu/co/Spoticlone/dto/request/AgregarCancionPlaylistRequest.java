package unbosque.edu.co.Spoticlone.dto.request;

import jakarta.validation.constraints.NotNull;

public class AgregarCancionPlaylistRequest {

    @NotNull(message = "El id de canción es obligatorio")
    private Integer idCancion;

    public Integer getIdCancion() { return idCancion; }
    public void setIdCancion(Integer idCancion) { this.idCancion = idCancion; }
}
