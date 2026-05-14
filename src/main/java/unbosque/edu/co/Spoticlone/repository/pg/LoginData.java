package unbosque.edu.co.Spoticlone.repository.pg;

/** Raw auth data fetched in a single query — internal to the auth flow. */
public final class LoginData {

    private final int idUsuario;
    private final String nombre;
    private final String passwordHash;
    private final String rol;

    public LoginData(int idUsuario, String nombre, String passwordHash, String rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    public int getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getPasswordHash() { return passwordHash; }
    public String getRol() { return rol; }
}
