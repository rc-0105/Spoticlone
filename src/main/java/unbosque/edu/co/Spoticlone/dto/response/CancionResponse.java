package unbosque.edu.co.Spoticlone.dto.response;

public class CancionResponse {

    private Integer idCancion;
    private String titulo;
    private Integer duracionSeg;
    private String urlPreview;
    private Integer numeroPista;
    private Integer idArtista;
    private String nomArtista;
    private Integer idGenero;
    private String nombreGenero;
    private String tituloAlbum;
    private Integer posicion; // para playlist_cancion (nullable)

    public CancionResponse() {}

    public Integer getIdCancion() { return idCancion; }
    public String getTitulo() { return titulo; }
    public Integer getDuracionSeg() { return duracionSeg; }
    public String getUrlPreview() { return urlPreview; }
    public Integer getNumeroPista() { return numeroPista; }
    public Integer getIdArtista() { return idArtista; }
    public String getNomArtista() { return nomArtista; }
    public Integer getIdGenero() { return idGenero; }
    public String getNombreGenero() { return nombreGenero; }
    public String getTituloAlbum() { return tituloAlbum; }
    public Integer getPosicion() { return posicion; }

    public void setIdCancion(Integer idCancion) { this.idCancion = idCancion; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDuracionSeg(Integer duracionSeg) { this.duracionSeg = duracionSeg; }
    public void setUrlPreview(String urlPreview) { this.urlPreview = urlPreview; }
    public void setNumeroPista(Integer numeroPista) { this.numeroPista = numeroPista; }
    public void setIdArtista(Integer idArtista) { this.idArtista = idArtista; }
    public void setNomArtista(String nomArtista) { this.nomArtista = nomArtista; }
    public void setIdGenero(Integer idGenero) { this.idGenero = idGenero; }
    public void setNombreGenero(String nombreGenero) { this.nombreGenero = nombreGenero; }
    public void setTituloAlbum(String tituloAlbum) { this.tituloAlbum = tituloAlbum; }
    public void setPosicion(Integer posicion) { this.posicion = posicion; }
}
