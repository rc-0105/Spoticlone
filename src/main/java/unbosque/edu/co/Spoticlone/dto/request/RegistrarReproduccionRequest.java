package unbosque.edu.co.Spoticlone.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class RegistrarReproduccionRequest {

    @NotNull(message = "El id de canción es obligatorio")
    private Integer idCancion;

    @NotNull(message = "La duración total es obligatoria")
    @Min(value = 1, message = "La duración total debe ser mayor a 0")
    private Integer duracionTotalSeg;

    @NotNull(message = "La duración escuchada es obligatoria")
    @Min(value = 1, message = "La duración escuchada debe ser mayor a 0")
    private Integer duracionEscuchadaSeg;

    private boolean completada;

    @NotBlank(message = "El dispositivo es obligatorio")
    @Pattern(regexp = "web|mobile|desktop", message = "El dispositivo debe ser 'web', 'mobile' o 'desktop'")
    private String dispositivo;

    @Valid
    @NotNull(message = "El contexto es obligatorio")
    private ContextoRequest contexto;

    public Integer getIdCancion() { return idCancion; }
    public Integer getDuracionTotalSeg() { return duracionTotalSeg; }
    public Integer getDuracionEscuchadaSeg() { return duracionEscuchadaSeg; }
    public boolean isCompletada() { return completada; }
    public String getDispositivo() { return dispositivo; }
    public ContextoRequest getContexto() { return contexto; }

    public void setIdCancion(Integer idCancion) { this.idCancion = idCancion; }
    public void setDuracionTotalSeg(Integer duracionTotalSeg) { this.duracionTotalSeg = duracionTotalSeg; }
    public void setDuracionEscuchadaSeg(Integer duracionEscuchadaSeg) { this.duracionEscuchadaSeg = duracionEscuchadaSeg; }
    public void setCompletada(boolean completada) { this.completada = completada; }
    public void setDispositivo(String dispositivo) { this.dispositivo = dispositivo; }
    public void setContexto(ContextoRequest contexto) { this.contexto = contexto; }
}
