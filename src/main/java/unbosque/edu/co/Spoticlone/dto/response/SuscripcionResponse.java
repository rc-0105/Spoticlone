package unbosque.edu.co.Spoticlone.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SuscripcionResponse {

    private Integer idSuscripcion;
    private Integer idUsuario;
    private String tipo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal precio;
    private Boolean activa;
    private Boolean tieneActiva;

    public SuscripcionResponse() {}

    public Integer getIdSuscripcion() { return idSuscripcion; }
    public Integer getIdUsuario() { return idUsuario; }
    public String getTipo() { return tipo; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public BigDecimal getPrecio() { return precio; }
    public Boolean getActiva() { return activa; }
    public Boolean getTieneActiva() { return tieneActiva; }

    public void setIdSuscripcion(Integer idSuscripcion) { this.idSuscripcion = idSuscripcion; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public void setActiva(Boolean activa) { this.activa = activa; }
    public void setTieneActiva(Boolean tieneActiva) { this.tieneActiva = tieneActiva; }
}
