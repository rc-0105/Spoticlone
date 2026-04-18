package unbosque.edu.co.Spoticlone.dto.response;

public class GeneroFavoritoResponse {

    private Integer idGenero;
    private String nombreGenero;
    private Integer totalReproducciones;

    public GeneroFavoritoResponse() {}

    public GeneroFavoritoResponse(Integer idGenero, String nombreGenero, Integer totalReproducciones) {
        this.idGenero = idGenero;
        this.nombreGenero = nombreGenero;
        this.totalReproducciones = totalReproducciones;
    }

    public Integer getIdGenero() { return idGenero; }
    public String getNombreGenero() { return nombreGenero; }
    public Integer getTotalReproducciones() { return totalReproducciones; }

    public void setIdGenero(Integer idGenero) { this.idGenero = idGenero; }
    public void setNombreGenero(String nombreGenero) { this.nombreGenero = nombreGenero; }
    public void setTotalReproducciones(Integer totalReproducciones) { this.totalReproducciones = totalReproducciones; }
}
