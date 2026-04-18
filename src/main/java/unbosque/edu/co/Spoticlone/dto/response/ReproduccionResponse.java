package unbosque.edu.co.Spoticlone.dto.response;

import java.util.Date;

public class ReproduccionResponse {

    private String id;
    private Integer idUsuario;
    private Integer idCancion;
    private String tituloCancion;
    private String nomArtista;
    private String nombreGenero;
    private Integer duracionTotalSeg;
    private Integer duracionEscuchadaSeg;
    private Boolean completada;
    private String dispositivo;
    private Date timestamp;

    public ReproduccionResponse() {}

    public String getId() { return id; }
    public Integer getIdUsuario() { return idUsuario; }
    public Integer getIdCancion() { return idCancion; }
    public String getTituloCancion() { return tituloCancion; }
    public String getNomArtista() { return nomArtista; }
    public String getNombreGenero() { return nombreGenero; }
    public Integer getDuracionTotalSeg() { return duracionTotalSeg; }
    public Integer getDuracionEscuchadaSeg() { return duracionEscuchadaSeg; }
    public Boolean getCompletada() { return completada; }
    public String getDispositivo() { return dispositivo; }
    public Date getTimestamp() { return timestamp; }

    public void setId(String id) { this.id = id; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public void setIdCancion(Integer idCancion) { this.idCancion = idCancion; }
    public void setTituloCancion(String tituloCancion) { this.tituloCancion = tituloCancion; }
    public void setNomArtista(String nomArtista) { this.nomArtista = nomArtista; }
    public void setNombreGenero(String nombreGenero) { this.nombreGenero = nombreGenero; }
    public void setDuracionTotalSeg(Integer duracionTotalSeg) { this.duracionTotalSeg = duracionTotalSeg; }
    public void setDuracionEscuchadaSeg(Integer duracionEscuchadaSeg) { this.duracionEscuchadaSeg = duracionEscuchadaSeg; }
    public void setCompletada(Boolean completada) { this.completada = completada; }
    public void setDispositivo(String dispositivo) { this.dispositivo = dispositivo; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
