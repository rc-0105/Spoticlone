package unbosque.edu.co.Spoticlone.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Login request DTO — email + plain password.
 * The password is sent over HTTPS and compared with BCrypt on the server.
 */
public class LoginRequest {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
}