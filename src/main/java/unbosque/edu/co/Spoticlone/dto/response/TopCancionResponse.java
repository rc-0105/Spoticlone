package unbosque.edu.co.Spoticlone.dto.response;

public class TopCancionResponse {

    private Integer idCancion;
    private String titulo;
    private String artista;
    private Integer totalReproducciones;
    private Integer oyentesUnicos;

    public TopCancionResponse() {}

    public TopCancionResponse(Integer idCancion, String titulo, String artista,
                              Integer totalReproducciones, Integer oyentesUnicos) {
        this.idCancion = idCancion;
        this.titulo = titulo;
        this.artista = artista;
        this.totalReproducciones = totalReproducciones;
        this.oyentesUnicos = oyentesUnicos;
    }

    public Integer getIdCancion() { return idCancion; }
    public String getTitulo() { return titulo; }
    public String getArtista() { return artista; }
    public Integer getTotalReproducciones() { return totalReproducciones; }
    public Integer getOyentesUnicos() { return oyentesUnicos; }

    public void setIdCancion(Integer idCancion) { this.idCancion = idCancion; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setArtista(String artista) { this.artista = artista; }
    public void setTotalReproducciones(Integer totalReproducciones) { this.totalReproducciones = totalReproducciones; }
    public void setOyentesUnicos(Integer oyentesUnicos) { this.oyentesUnicos = oyentesUnicos; }
}
