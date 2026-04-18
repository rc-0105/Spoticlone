package unbosque.edu.co.Spoticlone.dto.response;

import java.time.LocalDate;

public class AlbumResponse {

    private Integer idAlbum;
    private String titulo;
    private String tipo;
    private LocalDate fechaLanzamiento;
    private String portadaUrl;
    private Integer idArtista;
    private String nomArtista;

    public AlbumResponse() {}

    public Integer getIdAlbum() { return idAlbum; }
    public String getTitulo() { return titulo; }
    public String getTipo() { return tipo; }
    public LocalDate getFechaLanzamiento() { return fechaLanzamiento; }
    public String getPortadaUrl() { return portadaUrl; }
    public Integer getIdArtista() { return idArtista; }
    public String getNomArtista() { return nomArtista; }

    public void setIdAlbum(Integer idAlbum) { this.idAlbum = idAlbum; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setFechaLanzamiento(LocalDate fechaLanzamiento) { this.fechaLanzamiento = fechaLanzamiento; }
    public void setPortadaUrl(String portadaUrl) { this.portadaUrl = portadaUrl; }
    public void setIdArtista(Integer idArtista) { this.idArtista = idArtista; }
    public void setNomArtista(String nomArtista) { this.nomArtista = nomArtista; }
}
