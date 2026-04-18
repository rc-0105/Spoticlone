package unbosque.edu.co.Spoticlone.dto.response;

public class GeneroResponse {

    private Integer idGenero;
    private String nombre;
    private String descripcion;

    public GeneroResponse() {}

    public GeneroResponse(Integer idGenero, String nombre, String descripcion) {
        this.idGenero = idGenero;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getIdGenero() { return idGenero; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    public void setIdGenero(Integer idGenero) { this.idGenero = idGenero; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
