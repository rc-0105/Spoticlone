package unbosque.edu.co.Spoticlone.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CambiarSuscripcionRequest {

    @NotBlank(message = "El tipo de suscripción es obligatorio")
    @Pattern(regexp = "freemium|premium", message = "El tipo debe ser 'freemium' o 'premium'")
    private String nuevoTipo;

    public String getNuevoTipo() { return nuevoTipo; }
    public void setNuevoTipo(String nuevoTipo) { this.nuevoTipo = nuevoTipo; }
}
