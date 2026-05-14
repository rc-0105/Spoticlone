package unbosque.edu.co.Spoticlone.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CrearPlaylistRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "esPublica es obligatorio")
    private Boolean esPublica;

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Boolean getEsPublica() { return esPublica; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setEsPublica(Boolean esPublica) { this.esPublica = esPublica; }
}
