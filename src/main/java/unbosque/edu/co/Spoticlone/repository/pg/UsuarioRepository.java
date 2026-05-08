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
        return u;
    };

    /** Registra un nuevo usuario via stored procedure sp_registrar_usuario. */
    public void registrar(String nombre, String email, String passwordHash) {
        String sql = "CALL sp_registrar_usuario(?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, nombre);
            cs.setString(2, email);
            cs.setString(3, passwordHash);
            cs.execute();
        } catch (SQLException e) {
            String msg = e.getMessage() != null ? e.getMessage() : "Error al registrar usuario";
            if ("23505".equals(e.getSQLState()) || msg.toLowerCase().contains("ya exist")
                    || msg.toLowerCase().contains("already") || msg.toLowerCase().contains("duplicado")) {
                throw new BusinessException(msg, 409);
            }
            throw new BusinessException(msg, 400);
        }
    }

    /** Busca un usuario por ID. */
    public Optional<UsuarioResponse> findById(int idUsuario) {
        String sql = """
                SELECT id_usuario, nombre, email, foto_perfil, fecha_registro, activo
                FROM usuario
                WHERE id_usuario = ?
                """;
        List<UsuarioResponse> result = jdbc.query(sql, usuarioMapper, idUsuario);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    /** Lista todos los usuarios. */
    public List<UsuarioResponse> findAll() {
        String sql = """
                SELECT id_usuario, nombre, email, foto_perfil, fecha_registro, activo
                FROM usuario
                ORDER BY id_usuario
                """;
        return jdbc.query(sql, usuarioMapper);
    }

    /** Busca un usuario por email (para login). */
    public Optional<UsuarioResponse> findByEmail(String email) {
        String sql = """
                SELECT id_usuario, nombre, email, foto_perfil, fecha_registro, activo
                FROM usuario
                WHERE email = ?
                """;
        List<UsuarioResponse> result = jdbc.query(sql, usuarioMapper, email);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    /** Busca email y password_hash para autenticacion (solo uso interno). */
    public Optional<String> findPasswordHashByEmail(String email) {
        String sql = "SELECT password_hash FROM usuario WHERE email = ?";
        List<String> result = jdbc.query(sql, (rs, rowNum) -> rs.getString("password_hash"), email);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    /** Busca el rol de un usuario por email. */
    public Optional<String> findRolByEmail(String email) {
        String sql = "SELECT rol FROM usuario WHERE email = ?";
        List<String> result = jdbc.query(sql, (rs, rowNum) -> rs.getString("rol"), email);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
}
