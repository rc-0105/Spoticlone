package unbosque.edu.co.Spoticlone.repository.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import unbosque.edu.co.Spoticlone.dto.request.RegistrarReproduccionRequest;
import unbosque.edu.co.Spoticlone.dto.response.GeneroFavoritoResponse;
import unbosque.edu.co.Spoticlone.dto.response.ReproduccionResponse;
import unbosque.edu.co.Spoticlone.dto.response.TopCancionResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class ReproduccionRepository {

    private final MongoClient mongoClient;
    private final String database;

    public ReproduccionRepository(MongoClient mongoClient,
                                  @Value("${spoticlone.mongodb.database}") String database) {
        this.mongoClient = mongoClient;
        this.database = database;
    }

    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase(database).getCollection("reproducciones");
    }

    // ─── Insert ───────────────────────────────────────────────────────────────

    /** Registra una reproducción en MongoDB. */
    public void registrar(int idUsuario, RegistrarReproduccionRequest req) {
        Document contexto = new Document("tipo", req.getContexto().getTipo())
                .append("id_referencia", req.getContexto().getIdReferencia());

        Document doc = new Document("id_usuario", idUsuario)
                .append("id_cancion", req.getIdCancion())
                .append("id_artista", req.getIdArtista())
                .append("id_genero", req.getIdGenero())
                .append("titulo_cancion", req.getTituloCancion())
                .append("nom_artista", req.getNomArtista())
                .append("nombre_genero", req.getNombreGenero())
                .append("timestamp", new Date())
                .append("duracion_total_seg", req.getDuracionTotalSeg())
                .append("duracion_escuchada_seg", req.getDuracionEscuchadaSeg())
                .append("completada", req.isCompletada())
                .append("dispositivo", req.getDispositivo())
                .append("contexto", contexto);

        getCollection().insertOne(doc);
    }

    // ─── Historial ───────────────────────────────────────────────────────────

    /** Últimas 20 reproducciones del usuario, ordenadas por timestamp desc. */
    public List<ReproduccionResponse> historialByUsuario(int idUsuario) {
        List<ReproduccionResponse> result = new ArrayList<>();
        Bson filter = Filters.eq("id_usuario", idUsuario);
        Bson sort = Sorts.descending("timestamp");

        try (MongoCursor<Document> cursor = getCollection()
                .find(filter)
                .sort(sort)
                .limit(20)
                .iterator()) {
            while (cursor.hasNext()) {
                result.add(documentToReproduccion(cursor.next()));
            }
        }
        return result;
    }

    // ─── Top 10 canciones (agregación) ──────────────────────────────────────

    public List<TopCancionResponse> top10Canciones() {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.group("$id_cancion",
                        Accumulators.sum("totalReproducciones", 1),
                        Accumulators.addToSet("oyentes", "$id_usuario"),
                        Accumulators.first("titulo", "$titulo_cancion"),
                        Accumulators.first("artista", "$nom_artista")),
                Aggregates.addFields(
                        new Field<>("oyentesUnicos", new Document("$size", "$oyentes"))),
                Aggregates.sort(Sorts.descending("totalReproducciones")),
                Aggregates.limit(10)
        );

        List<TopCancionResponse> result = new ArrayList<>();
        try (MongoCursor<Document> cursor = getCollection()
                .aggregate(pipeline).iterator()) {
            while (cursor.hasNext()) {
                Document d = cursor.next();
                result.add(new TopCancionResponse(
                        d.getInteger("_id"),
                        d.getString("titulo"),
                        d.getString("artista"),
                        d.getInteger("totalReproducciones"),
                        d.getInteger("oyentesUnicos")
                ));
            }
        }
        return result;
    }

    // ─── Género favorito (agregación) ────────────────────────────────────────

    public List<GeneroFavoritoResponse> generoFavorito(int idUsuario) {
        List<Bson> pipeline = Arrays.asList(
                Aggregates.match(Filters.eq("id_usuario", idUsuario)),
                Aggregates.group("$id_genero",
                        Accumulators.sum("total", 1),
                        Accumulators.first("nombreGenero", "$nombre_genero")),
                Aggregates.sort(Sorts.descending("total")),
                Aggregates.limit(1)
        );

        List<GeneroFavoritoResponse> result = new ArrayList<>();
        try (MongoCursor<Document> cursor = getCollection()
                .aggregate(pipeline).iterator()) {
            while (cursor.hasNext()) {
                Document d = cursor.next();
                result.add(new GeneroFavoritoResponse(
                        d.getInteger("_id"),
                        d.getString("nombreGenero"),
                        d.getInteger("total")
                ));
            }
        }
        return result;
    }

    // ─── Mapper Document → DTO ───────────────────────────────────────────────

    private ReproduccionResponse documentToReproduccion(Document d) {
        ReproduccionResponse r = new ReproduccionResponse();
        r.setId(d.getObjectId("_id") != null ? d.getObjectId("_id").toString() : null);
        r.setIdUsuario(d.getInteger("id_usuario"));
        r.setIdCancion(d.getInteger("id_cancion"));
        r.setTituloCancion(d.getString("titulo_cancion"));
        r.setNomArtista(d.getString("nom_artista"));
        r.setNombreGenero(d.getString("nombre_genero"));
        r.setDuracionTotalSeg(d.getInteger("duracion_total_seg"));
        r.setDuracionEscuchadaSeg(d.getInteger("duracion_escuchada_seg"));
        r.setCompletada(d.getBoolean("completada", false));
        r.setDispositivo(d.getString("dispositivo"));
        r.setTimestamp(d.getDate("timestamp"));
        return r;
    }
}
