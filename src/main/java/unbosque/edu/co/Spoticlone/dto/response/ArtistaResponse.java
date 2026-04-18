package unbosque.edu.co.Spoticlone.dto.response;

import java.time.LocalDate;

public class ArtistaResponse {

    private Integer idArtista;
    private String nomArtistico;
    private String pais;
    private String biografia;
    private String fotoUrl;
    private LocalDate fechaRegistro;
    private Integer totalCanciones;

    public ArtistaResponse() {}

    public Integer getIdArtista() { return idArtista; }
    public String getNomArtistico() { return nomArtistico; }
    public String getPais() { return pais; }
    public String getBiografia() { return biografia; }
    public String getFotoUrl() { return fotoUrl; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public Integer getTotalCanciones() { return totalCanciones; }

    public void setIdArtista(Integer idArtista) { this.idArtista = idArtista; }
    public void setNomArtistico(String nomArtistico) { this.nomArtistico = nomArtistico; }
    public void setPais(String pais) { this.pais = pais; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public void setTotalCanciones(Integer totalCanciones) { this.totalCanciones = totalCanciones; }
}
