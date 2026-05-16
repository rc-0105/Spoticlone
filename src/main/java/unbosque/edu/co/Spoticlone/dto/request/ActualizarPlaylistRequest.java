package unbosque.edu.co.Spoticlone.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ActualizarPlaylistRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    private boolean esPublica;

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public boolean isEsPublica() { return esPublica; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setEsPublica(boolean esPublica) { this.esPublica = esPublica; }
}
