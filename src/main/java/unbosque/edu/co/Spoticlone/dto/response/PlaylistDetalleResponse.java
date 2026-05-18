package unbosque.edu.co.Spoticlone.dto.response;

import java.time.LocalDate;
import java.util.List;

public class PlaylistDetalleResponse {

    private Integer idPlaylist;
    private Integer idUsuario;
    private String nombre;
    private String descripcion;
    private Boolean esPublica;
    private LocalDate fechaCreacion;
    private Integer totalCanciones;
    private String nombreUsuario;
    private Integer duracionTotalSeg;
    private List<CancionResponse> canciones;

    public PlaylistDetalleResponse() {}

    public Integer getIdPlaylist() { return idPlaylist; }
    public Integer getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Boolean getEsPublica() { return esPublica; }
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public Integer getTotalCanciones() { return totalCanciones; }
    public String getNombreUsuario() { return nombreUsuario; }
    public Integer getDuracionTotalSeg() { return duracionTotalSeg; }
    public List<CancionResponse> getCanciones() { return canciones; }

    public void setIdPlaylist(Integer idPlaylist) { this.idPlaylist = idPlaylist; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setEsPublica(Boolean esPublica) { this.esPublica = esPublica; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public void setTotalCanciones(Integer totalCanciones) { this.totalCanciones = totalCanciones; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public void setDuracionTotalSeg(Integer duracionTotalSeg) { this.duracionTotalSeg = duracionTotalSeg; }
    public void setCanciones(List<CancionResponse> canciones) { this.canciones = canciones; }
}
