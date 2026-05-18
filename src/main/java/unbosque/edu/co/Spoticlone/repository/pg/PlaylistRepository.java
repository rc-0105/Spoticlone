package unbosque.edu.co.Spoticlone.repository.pg;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import unbosque.edu.co.Spoticlone.dto.response.CancionResponse;
import unbosque.edu.co.Spoticlone.dto.response.PlaylistDetalleResponse;
import unbosque.edu.co.Spoticlone.dto.response.PlaylistResponse;
import unbosque.edu.co.Spoticlone.exception.BusinessException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
public class PlaylistRepository {

    private final DataSource dataSource;
    private final JdbcTemplate jdbc;

    public PlaylistRepository(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbc = jdbcTemplate;
    }

    // ─── Mappers ─────────────────────────────────────────────────────────────

    private final RowMapper<PlaylistResponse> playlistMapper = (rs, rowNum) -> {
        PlaylistResponse p = new PlaylistResponse();
        p.setIdPlaylist(rs.getInt("id_playlist"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setEsPublica(rs.getBoolean("es_publica"));
        Date fecha = rs.getDate("fecha_creacion");
        if (fecha != null) p.setFechaCreacion(fecha.toLocalDate());
        p.setTotalCanciones(rs.getInt("total_canciones"));
        try { p.setNombreUsuario(rs.getString("nombre_usuario")); } catch (SQLException ignored) {}
        return p;
    };

    private final RowMapper<CancionResponse> cancionPlaylistMapper = (rs, rowNum) -> {
        CancionResponse c = new CancionResponse();
        c.setIdCancion(rs.getInt("id_cancion"));
        c.setTitulo(rs.getString("titulo"));
        c.setDuracionSeg(rs.getInt("duracion_seg"));
        c.setUrlPreview(rs.getString("url_preview"));
        c.setNumeroPista(rs.getInt("numero_pista"));
        c.setIdArtista(rs.getInt("id_artista"));
        c.setNomArtista(rs.getString("nom_artistico"));
        c.setNombreGenero(rs.getString("nombre_genero"));
        c.setTituloAlbum(rs.getString("titulo_album"));
        c.setPosicion(rs.getInt("posicion"));
        return c;
    };

    // ─── Stored Procedures ────────────────────────────────────────────────────

    /** Crea una playlist via sp_crear_playlist. */
    public void crearPlaylist(int idUsuario, String nombre, String descripcion, boolean esPublica) {
        String sql = "CALL sp_crear_playlist(?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(true);
            try (CallableStatement cs = conn.prepareCall(sql)) {
                cs.setInt(1, idUsuario);
                cs.setString(2, nombre);
                cs.setString(3, descripcion);
                cs.setBoolean(4, esPublica);
                cs.execute();
            }
        } catch (SQLException e) {
            String msg = extractSPMessage(e.getMessage()) != null ? extractSPMessage(e.getMessage()) : "Error al crear playlist";
            throw new BusinessException(msg, 400);
        }
    }

    /** Elimina una canción de una playlist via sp_eliminar_cancion_playlist. */
    public void eliminarCancion(int idPlaylist, int idCancion) {
        String sql = "CALL sp_eliminar_cancion_playlist(?, ?)";
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(true);
            try (CallableStatement cs = conn.prepareCall(sql)) {
                cs.setInt(1, idPlaylist);
                cs.setInt(2, idCancion);
                cs.execute();
            }
        } catch (SQLException e) {
            String msg = extractSPMessage(e.getMessage()) != null ? extractSPMessage(e.getMessage()) : "Error al eliminar canción";
            throw new BusinessException(msg, 400);
        }
    }

    /** Agrega una canción a una playlist via sp_agregar_cancion_playlist. */
    public void agregarCancion(int idPlaylist, int idCancion) {
        String sql = "CALL sp_agregar_cancion_playlist(?, ?)";
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(true);
            try (CallableStatement cs = conn.prepareCall(sql)) {
                cs.setInt(1, idPlaylist);
                cs.setInt(2, idCancion);
                cs.execute();
            }
        } catch (SQLException e) {
            String msg = extractSPMessage(e.getMessage()) != null ? extractSPMessage(e.getMessage()) : "Error al agregar canción";
            if ("23505".equals(e.getSQLState()) || (msg.toLowerCase().contains("ya")
                    && msg.toLowerCase().contains("playlist"))) {
                throw new BusinessException(msg, 409);
            }
            throw new BusinessException(msg, 400);
        }
    }

    // ─── Función BD: duración total ──────────────────────────────────────────

    /** Llama a fn_duracion_total_playlist y retorna segundos totales. */
    public int duracionTotal(int idPlaylist) {
        String sql = "{ ? = call fn_duracion_total_playlist(?) }";
        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, idPlaylist);
            cs.execute();
            return cs.getInt(1);
        } catch (SQLException e) {
            throw new BusinessException(extractSPMessage(e.getMessage()));
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

    /** Elimina todas las canciones de una playlist y luego la playlist. */
    public void eliminarPlaylist(int idPlaylist) {
        jdbc.update("DELETE FROM playlist_cancion WHERE id_playlist = ?", idPlaylist);
        jdbc.update("DELETE FROM playlist WHERE id_playlist = ?", idPlaylist);
    }

    /** Actualiza nombre, descripción y visibilidad de una playlist. */
    public PlaylistDetalleResponse actualizarPlaylist(int idPlaylist, String nombre, String descripcion, boolean esPublica) {
        jdbc.update(
                "UPDATE playlist SET nombre = ?, descripcion = ?, es_publica = ? WHERE id_playlist = ?",
                nombre, descripcion, esPublica, idPlaylist
        );
        return findById(idPlaylist)
                .orElseThrow(() -> new BusinessException("Playlist no encontrada tras actualizar", 404));
    }

    // ─── Consultas SELECT ────────────────────────────────────────────────────

    /** Retorna el id_usuario dueño de una playlist, o empty si no existe. */
    public Optional<Integer> findIdUsuarioByPlaylist(int idPlaylist) {
        String sql = "SELECT id_usuario FROM playlist WHERE id_playlist = ?";
        List<Integer> result = jdbc.query(sql,
                (rs, rowNum) -> rs.getInt("id_usuario"), idPlaylist);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    /** Detalle completo de una playlist (sin canciones — se obtienen aparte). */
    public Optional<PlaylistDetalleResponse> findById(int idPlaylist) {
        String sql = """
                SELECT p.id_playlist, p.id_usuario, p.nombre, p.descripcion, p.es_publica,
                       p.fecha_creacion, p.total_canciones, u.nombre AS nombre_usuario
                FROM playlist p
                JOIN usuario u ON p.id_usuario = u.id_usuario
                WHERE p.id_playlist = ?
                """;
        List<PlaylistDetalleResponse> result = jdbc.query(sql, (rs, rowNum) -> {
            PlaylistDetalleResponse pd = new PlaylistDetalleResponse();
            pd.setIdPlaylist(rs.getInt("id_playlist"));
            pd.setIdUsuario(rs.getInt("id_usuario"));
            pd.setNombre(rs.getString("nombre"));
            pd.setDescripcion(rs.getString("descripcion"));
            pd.setEsPublica(rs.getBoolean("es_publica"));
            Date fecha = rs.getDate("fecha_creacion");
            if (fecha != null) pd.setFechaCreacion(fecha.toLocalDate());
            pd.setTotalCanciones(rs.getInt("total_canciones"));
            pd.setNombreUsuario(rs.getString("nombre_usuario"));
            return pd;
        }, idPlaylist);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    /** Canciones de una playlist ordenadas por posición. */
    public List<CancionResponse> findCancionesDePlaylist(int idPlaylist) {
        String sql = """
                SELECT c.id_cancion, c.titulo, c.duracion_seg, c.url_preview,
                       c.numero_pista, a.id_artista, a.nom_artistico,
                       g.nombre AS nombre_genero, al.titulo AS titulo_album,
                       pc.posicion
                FROM playlist_cancion pc
                JOIN cancion c ON pc.id_cancion = c.id_cancion
                JOIN album al ON c.id_album = al.id_album
                JOIN artista a ON al.id_artista = a.id_artista
                JOIN genero g ON c.id_genero = g.id_genero
                WHERE pc.id_playlist = ?
                ORDER BY pc.posicion
                """;
        return jdbc.query(sql, cancionPlaylistMapper, idPlaylist);
    }

    /** Playlists de un usuario específico. */
    public List<PlaylistResponse> findByUsuario(int idUsuario) {
        String sql = """
                SELECT id_playlist, nombre, descripcion, es_publica,
                       fecha_creacion, total_canciones
                FROM playlist
                WHERE id_usuario = ?
                ORDER BY fecha_creacion DESC
                """;
        return jdbc.query(sql, (rs, rowNum) -> {
            PlaylistResponse p = new PlaylistResponse();
            p.setIdPlaylist(rs.getInt("id_playlist"));
            p.setNombre(rs.getString("nombre"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setEsPublica(rs.getBoolean("es_publica"));
            Date fecha = rs.getDate("fecha_creacion");
            if (fecha != null) p.setFechaCreacion(fecha.toLocalDate());
            p.setTotalCanciones(rs.getInt("total_canciones"));
            return p;
        }, idUsuario);
    }

    /** Lista playlists públicas con nombre del usuario. */
    public List<PlaylistResponse> findPublicas() {
        String sql = """
                SELECT p.id_playlist, p.nombre, p.descripcion, p.es_publica,
                       p.fecha_creacion, p.total_canciones, u.nombre AS nombre_usuario
                FROM playlist p
                JOIN usuario u ON p.id_usuario = u.id_usuario
                WHERE p.es_publica = true
                ORDER BY p.fecha_creacion DESC
                """;
        return jdbc.query(sql, playlistMapper);
    }
}
