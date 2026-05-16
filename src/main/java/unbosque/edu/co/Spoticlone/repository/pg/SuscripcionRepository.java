package unbosque.edu.co.Spoticlone.repository.pg;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import unbosque.edu.co.Spoticlone.dto.response.SuscripcionResponse;
import unbosque.edu.co.Spoticlone.exception.BusinessException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
public class SuscripcionRepository {

    private final DataSource dataSource;
    private final JdbcTemplate jdbc;

    public SuscripcionRepository(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbc = jdbcTemplate;
    }

    /**
     * Cambia el plan del usuario via sp_cambiar_suscripcion.
     * El procedure actualiza precio, fecha_fin y escribe en auditoria_log.
     */
    public void cambiarSuscripcion(int idUsuario, String nuevoTipo) {
        String sql = "CALL sp_cambiar_suscripcion(?, ?)";
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(true);
            try (CallableStatement cs = conn.prepareCall(sql)) {
                cs.setInt(1, idUsuario);
                cs.setString(2, nuevoTipo);
                cs.execute();
            }
        } catch (SQLException e) {
            String msg = extractSPMessage(e.getMessage()) != null ? extractSPMessage(e.getMessage()) : "Error al cambiar suscripción";
            throw new BusinessException(msg, 400);
        }
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

    /**
     * Obtiene el estado de suscripción de un usuario,
     * incluyendo fn_tiene_suscripcion_activa.
     */
    public Optional<SuscripcionResponse> findByUsuario(int idUsuario) {
        String sql = """
                SELECT s.id_suscripcion, s.id_usuario, s.tipo,
                       s.fecha_inicio, s.fecha_fin, s.precio, s.activa,
                       fn_tiene_suscripcion_activa(s.id_usuario) AS tiene_activa
                FROM suscripcion s
                WHERE s.id_usuario = ?
                """;
        List<SuscripcionResponse> result = jdbc.query(sql, (rs, rowNum) -> {
            SuscripcionResponse sr = new SuscripcionResponse();
            sr.setIdSuscripcion(rs.getInt("id_suscripcion"));
            sr.setIdUsuario(rs.getInt("id_usuario"));
            sr.setTipo(rs.getString("tipo"));
            Date fi = rs.getDate("fecha_inicio");
            if (fi != null) sr.setFechaInicio(fi.toLocalDate());
            Date ff = rs.getDate("fecha_fin");
            if (ff != null) sr.setFechaFin(ff.toLocalDate());
            sr.setPrecio(rs.getBigDecimal("precio"));
            sr.setActiva(rs.getBoolean("activa"));
            sr.setTieneActiva(rs.getBoolean("tiene_activa"));
            return sr;
        }, idUsuario);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
}
