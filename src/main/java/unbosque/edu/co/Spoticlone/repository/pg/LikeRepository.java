package unbosque.edu.co.Spoticlone.repository.pg;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import unbosque.edu.co.Spoticlone.dto.response.CancionResponse;
import unbosque.edu.co.Spoticlone.exception.BusinessException;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Repository
public class LikeRepository {

    private final DataSource dataSource;
    private final JdbcTemplate jdbc;

    public LikeRepository(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbc = jdbcTemplate;
    }

    private final RowMapper<CancionResponse> cancionMapper = (rs, rowNum) -> {
        CancionResponse c = new CancionResponse();
        c.setIdCancion(rs.getInt("id_cancion"));
        c.setTitulo(rs.getString("titulo"));
        c.setDuracionSeg(rs.getInt("duracion_seg"));
        c.setUrlPreview(rs.getString("url_preview"));
        c.setNumeroPista(rs.getInt("numero_pista"));
        c.setIdArtista(rs.getInt("id_artista"));
        c.setNomArtista(rs.getString("nom_artistico"));
        c.setIdGenero(rs.getInt("id_genero"));
        c.setNombreGenero(rs.getString("nombre_genero"));
        c.setTituloAlbum(rs.getString("titulo_album"));
        return c;
    };

    public void darLike(int idUsuario, int idCancion) {
        String sql = "CALL sp_dar_like(?, ?)";
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(true);
            try (CallableStatement cs = conn.prepareCall(sql)) {
                cs.setInt(1, idUsuario);
                cs.setInt(2, idCancion);
                cs.execute();
            }
        } catch (SQLException e) {
            String msg = extractSPMessage(e.getMessage()) != null ? extractSPMessage(e.getMessage()) : "Error al registrar like";
            if (msg.toLowerCase().contains("ya le dio like")) {
                throw new BusinessException(msg, 409);
            }
            throw new BusinessException(msg, 400);
        }
    }

    public void quitarLike(int idUsuario, int idCancion) {
        String sql = "CALL sp_quitar_like(?, ?)";
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(true);
            try (CallableStatement cs = conn.prepareCall(sql)) {
                cs.setInt(1, idUsuario);
                cs.setInt(2, idCancion);
                cs.execute();
            }
        } catch (SQLException e) {
            String msg = extractSPMessage(e.getMessage()) != null ? extractSPMessage(e.getMessage()) : "Error al quitar like";
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

    public List<CancionResponse> findFavoritosByUsuario(int idUsuario) {
        String sql = """
                SELECT c.id_cancion, c.titulo, c.duracion_seg, c.url_preview, c.numero_pista,
                       a.id_artista, a.nom_artistico,
                       g.id_genero, g.nombre AS nombre_genero,
                       al.titulo AS titulo_album
                FROM like_cancion lk
                JOIN cancion c  ON lk.id_cancion  = c.id_cancion
                JOIN album al   ON c.id_album      = al.id_album
                JOIN artista a  ON al.id_artista   = a.id_artista
                JOIN genero g   ON c.id_genero     = g.id_genero
                WHERE lk.id_usuario = ?
                ORDER BY lk.fecha_like DESC
                """;
        return jdbc.query(sql, cancionMapper, idUsuario);
    }
}
