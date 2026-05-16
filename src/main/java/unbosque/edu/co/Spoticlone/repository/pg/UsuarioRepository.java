package unbosque.edu.co.Spoticlone.repository.pg;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import unbosque.edu.co.Spoticlone.dto.response.UsuarioResponse;
import unbosque.edu.co.Spoticlone.exception.BusinessException;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class UsuarioRepository {

    private final DataSource dataSource;
    private final JdbcTemplate jdbc;

    public UsuarioRepository(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbc = jdbcTemplate;
    }

    private final RowMapper<UsuarioResponse> usuarioMapper = (rs, rowNum) -> {
        UsuarioResponse u = new UsuarioResponse();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNombre(rs.getString("nombre"));
        u.setEmail(rs.getString("email"));
        u.setFotoPerfil(rs.getString("foto_perfil"));
        Date fecha = rs.getDate("fecha_registro");
        if (fecha != null) u.setFechaRegistro(fecha.toLocalDate());
        u.setActivo(rs.getBoolean("activo"));
        u.setRol(rs.getString("rol"));
        return u;
    };

    /** Registra un nuevo usuario via stored procedure sp_registrar_usuario. */
    public void registrar(String nombre, String email, String passwordHash) {
        String sql = "CALL sp_registrar_usuario(?, ?, ?)";
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(true);
            try (CallableStatement cs = conn.prepareCall(sql)) {
                cs.setString(1, nombre);
                cs.setString(2, email);
                cs.setString(3, passwordHash);
                cs.execute();
            }
        } catch (SQLException e) {
            String msg = extractSPMessage(e.getMessage()) != null ? extractSPMessage(e.getMessage()) : "Error al registrar usuario";
            if ("23505".equals(e.getSQLState()) || msg.toLowerCase().contains("ya exist")
                    || msg.toLowerCase().contains("ya est")
                    || msg.toLowerCase().contains("already") || msg.toLowerCase().contains("duplicado")) {
                throw new BusinessException(msg, 409);
            }
            throw new BusinessException(msg, 400);
        }
    }

    /** Busca un usuario por ID. */
    public Optional<UsuarioResponse> findById(int idUsuario) {
        String sql = """
                SELECT id_usuario, nombre, email, foto_perfil, fecha_registro, activo, rol
                FROM usuario
                WHERE id_usuario = ?
                """;
        List<UsuarioResponse> result = jdbc.query(sql, usuarioMapper, idUsuario);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    /** Lista todos los usuarios. */
    public List<UsuarioResponse> findAll() {
        String sql = """
                SELECT id_usuario, nombre, email, foto_perfil, fecha_registro, activo, rol
                FROM usuario
                ORDER BY id_usuario
                """;
        return jdbc.query(sql, usuarioMapper);
    }

    /** Busca un usuario por email (para login). */
    public Optional<UsuarioResponse> findByEmail(String email) {
        String sql = """
                SELECT id_usuario, nombre, email, foto_perfil, fecha_registro, activo, rol
                FROM usuario
                WHERE email = ?
                """;
        List<UsuarioResponse> result = jdbc.query(sql, usuarioMapper, email);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    /** Fetches all auth-relevant fields in a single query for login. */
    public Optional<LoginData> findForLogin(String email) {
        String sql = """
                SELECT id_usuario, nombre, password_hash, rol
                FROM usuario
                WHERE email = ?
                """;
        List<LoginData> result = jdbc.query(sql, (rs, rowNum) -> new LoginData(
                rs.getInt("id_usuario"),
                rs.getString("nombre"),
                rs.getString("password_hash"),
                rs.getString("rol")
        ), email);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    /** Busca el rol de un usuario por email. */
    public Optional<String> findRolByEmail(String email) {
        String sql = "SELECT rol FROM usuario WHERE email = ?";
        List<String> result = jdbc.query(sql, (rs, rowNum) -> rs.getString("rol"), email);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    private static String extractSPMessage(String raw) {
        if (raw == null) return "Error en la operación";
        String msg = raw.replaceFirst("^ERROR:\\s*", "");
        msg = msg.replaceFirst("^\\S+ falló:\\s*", "");
        int idx = msg.indexOf("\n  Where:");
        if (idx < 0) idx = msg.indexOf("\n  Detail:");
        if (idx < 0) idx = msg.indexOf("\n  Hint:");
        if (idx > 0) msg = msg.substring(0, idx);
        return msg.trim();
    }
}
