package unbosque.edu.co.Spoticlone.dto.response;

import java.time.LocalDate;

public class UsuarioResponse {

    private Integer idUsuario;
    private String nombre;
    private String email;
    private String fotoPerfil;
    private LocalDate fechaRegistro;
    private Boolean activo;

    public UsuarioResponse() {}

    public UsuarioResponse(Integer idUsuario, String nombre, String email,
                           String fotoPerfil, LocalDate fechaRegistro, Boolean activo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.fotoPerfil = fotoPerfil;
        this.fechaRegistro = fechaRegistro;
        this.activo = activo;
    }

    public Integer getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getFotoPerfil() { return fotoPerfil; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public Boolean getActivo() { return activo; }

    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
