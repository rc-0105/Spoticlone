package unbosque.edu.co.Spoticlone.repository.pg;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import unbosque.edu.co.Spoticlone.dto.response.AlbumResponse;
import unbosque.edu.co.Spoticlone.dto.response.ArtistaResponse;
import unbosque.edu.co.Spoticlone.dto.response.CancionResponse;
import unbosque.edu.co.Spoticlone.dto.response.GeneroResponse;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CatalogRepository {

    private final JdbcTemplate jdbc;

    public CatalogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    // ─── Mappers ─────────────────────────────────────────────────────────────

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

    private final RowMapper<ArtistaResponse> artistaMapper = (rs, rowNum) -> {
        ArtistaResponse a = new ArtistaResponse();
        a.setIdArtista(rs.getInt("id_artista"));
        a.setNomArtistico(rs.getString("nom_artistico"));
        a.setPais(rs.getString("pais"));
        a.setBiografia(rs.getString("biografia"));
        a.setFotoUrl(rs.getString("foto_url"));
        Date fecha = rs.getDate("fecha_registro");
        if (fecha != null) a.setFechaRegistro(fecha.toLocalDate());
        a.setTotalCanciones(rs.getInt("total_canciones"));
        return a;
    };

    private final RowMapper<GeneroResponse> generoMapper = (rs, rowNum) ->
            new GeneroResponse(
                    rs.getInt("id_genero"),
                    rs.getString("nombre"),
                    rs.getString("descripcion")
            );

    private final RowMapper<AlbumResponse> albumMapper = (rs, rowNum) -> {
        AlbumResponse al = new AlbumResponse();
        al.setIdAlbum(rs.getInt("id_album"));
        al.setTitulo(rs.getString("titulo_album"));
        al.setTipo(rs.getString("tipo"));
        Date fecha = rs.getDate("fecha_lanzamiento");
        if (fecha != null) al.setFechaLanzamiento(fecha.toLocalDate());
        al.setPortadaUrl(rs.getString("portada_url"));
        al.setIdArtista(rs.getInt("id_artista"));
        al.setNomArtista(rs.getString("nom_artistico"));
        return al;
    };

    // ─── Canciones ───────────────────────────────────────────────────────────

    private static final String BASE_CANCION_SQL = """
            SELECT c.id_cancion, c.titulo, c.duracion_seg, c.url_preview, c.numero_pista,
                   a.id_artista, a.nom_artistico,
                   g.id_genero, g.nombre AS nombre_genero,
                   al.titulo AS titulo_album
            FROM cancion c
            JOIN album al ON c.id_album = al.id_album
            JOIN artista a ON al.id_artista = a.id_artista
            JOIN genero g ON c.id_genero = g.id_genero
            """;

    /** Lista canciones con filtros opcionales por género y artista. */
    public List<CancionResponse> findCanciones(String genero, String artista) {
        StringBuilder sql = new StringBuilder(BASE_CANCION_SQL);
        List<Object> params = new ArrayList<>();

        if (genero != null && !genero.isBlank()) {
            sql.append(" WHERE g.nombre ILIKE ?");
            params.add("%" + genero + "%");
            if (artista != null && !artista.isBlank()) {
                sql.append(" AND a.nom_artistico ILIKE ?");
                params.add("%" + artista + "%");
            }
        } else if (artista != null && !artista.isBlank()) {
            sql.append(" WHERE a.nom_artistico ILIKE ?");
            params.add("%" + artista + "%");
        }
        sql.append(" ORDER BY c.id_cancion");
        return jdbc.query(sql.toString(), cancionMapper, params.toArray());
    }

    /** Canciones de un álbum específico, ordenadas por número de pista. */
    public List<CancionResponse> findCancionesByAlbum(int idAlbum) {
        String sql = BASE_CANCION_SQL + " WHERE c.id_album = ? ORDER BY c.numero_pista";
        return jdbc.query(sql, cancionMapper, idAlbum);
    }

    // ─── Artistas ─────────────────────────────────────────────────────────────

    /** Lista todos los artistas con total de canciones (via función de BD). */
    public List<ArtistaResponse> findArtistas() {
        String sql = """
                SELECT a.id_artista, a.nom_artistico, a.pais, a.biografia, a.foto_url,
                       a.fecha_registro, fn_canciones_por_artista(a.id_artista) AS total_canciones
                FROM artista a
                ORDER BY a.nom_artistico
                """;
        return jdbc.query(sql, artistaMapper);
    }

    /** Detalle de un artista por ID. */
    public Optional<ArtistaResponse> findArtistaById(int idArtista) {
        String sql = """
                SELECT a.id_artista, a.nom_artistico, a.pais, a.biografia, a.foto_url,
                       a.fecha_registro, fn_canciones_por_artista(a.id_artista) AS total_canciones
                FROM artista a
                WHERE a.id_artista = ?
                """;
        List<ArtistaResponse> result = jdbc.query(sql, artistaMapper, idArtista);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    // ─── Géneros ──────────────────────────────────────────────────────────────

    public List<GeneroResponse> findGeneros() {
        return jdbc.query(
                "SELECT id_genero, nombre, descripcion FROM genero ORDER BY nombre",
                generoMapper);
    }

    // ─── Álbumes ──────────────────────────────────────────────────────────────

    public List<AlbumResponse> findAlbumes() {
        String sql = """
                SELECT al.id_album, al.titulo AS titulo_album, al.tipo,
                       al.fecha_lanzamiento, al.portada_url,
                       a.id_artista, a.nom_artistico
                FROM album al
                JOIN artista a ON al.id_artista = a.id_artista
                ORDER BY al.fecha_lanzamiento DESC
                """;
        return jdbc.query(sql, albumMapper);
    }
}
