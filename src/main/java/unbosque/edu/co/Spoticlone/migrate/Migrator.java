package unbosque.edu.co.Spoticlone.migrate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

/**
 * Sistema de migraciones de base de datos para SpotiClone.
 *
 * Variables de entorno requeridas:
 *   DB_URL      → jdbc:postgresql://localhost:5432/spoticlone
 *   DB_USER     → usuario de PostgreSQL
 *   DB_PASSWORD → contraseña
 *
 * Variable opcional:
 *   SQL_DIR     → ruta a la carpeta con los archivos SQL (default: "sql")
 *
 * Ejecutar desde la raíz del proyecto:
 *   mvn exec:java -Dexec.mainClass=unbosque.edu.co.Spoticlone.migrate.Migrator
 */
public class Migrator {

    private static final String CREATE_HISTORY_TABLE = """
            CREATE TABLE IF NOT EXISTS migration_history (
                id          SERIAL PRIMARY KEY,
                filename    VARCHAR(255) NOT NULL UNIQUE,
                executed_at TIMESTAMP NOT NULL DEFAULT NOW(),
                checksum    VARCHAR(64)
            )
            """;

    public static void main(String[] args) {
        String dbUrl      = System.getenv("DB_URL");
        String dbUser     = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        if (dbUrl == null || dbUser == null || dbPassword == null) {
            System.err.println("[MIGRATOR] Error: faltan variables de entorno.");
            System.err.println("[MIGRATOR]   DB_URL      = " + (dbUrl      != null ? dbUrl      : "(no definida)"));
            System.err.println("[MIGRATOR]   DB_USER     = " + (dbUser     != null ? dbUser     : "(no definida)"));
            System.err.println("[MIGRATOR]   DB_PASSWORD = " + (dbPassword != null ? "***"       : "(no definida)"));
            System.exit(1);
        }

        String sqlDirPath = System.getenv().getOrDefault("SQL_DIR", "sql");
        File   sqlDir     = new File(sqlDirPath);

        if (!sqlDir.exists() || !sqlDir.isDirectory()) {
            System.err.println("[MIGRATOR] Error: la carpeta SQL no existe: " + sqlDir.getAbsolutePath());
            System.err.println("[MIGRATOR]   Asegurate de ejecutar desde la raíz del proyecto Maven.");
            System.exit(1);
        }

        try {
            run(dbUrl, dbUser, dbPassword, sqlDir);
        } catch (Exception e) {
            System.err.println("[MIGRATOR] Error inesperado: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void run(String dbUrl, String dbUser, String dbPassword, File sqlDir)
            throws IOException, NoSuchAlgorithmException {

        // autoCommit=true: los stored procedures del DML hacen COMMIT interno.
        // Ejecutarlos dentro de un BEGIN explícito causaría error en PostgreSQL.
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            conn.setAutoCommit(true);

            createHistoryTable(conn);

            File[] sqlFiles = discoverSqlFiles(sqlDir);
            if (sqlFiles.length == 0) {
                System.out.println("[MIGRATOR] No se encontraron archivos .sql en: " + sqlDir.getAbsolutePath());
                return;
            }

            Map<String, String> history = loadHistory(conn);
            int executed = 0;

            for (File file : sqlFiles) {
                String filename = file.getName();
                String content  = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                String checksum = sha256(content);

                if (history.containsKey(filename)) {
                    warnIfChecksumChanged(filename, checksum, history.get(filename));
                    continue;
                }

                System.out.printf("[MIGRATOR] Ejecutando: %s ...%n", filename);
                executeSqlFile(conn, filename, content);
                recordInHistory(conn, filename, checksum);
                System.out.printf("[MIGRATOR] ✓ %s ejecutado correctamente%n", filename);
                executed++;
            }

            if (executed == 0) {
                System.out.println("[MIGRATOR] ✓ Base de datos actualizada. Sin migraciones pendientes.");
            } else {
                System.out.printf("[MIGRATOR] ✓ Migración completada. %d archivo(s) ejecutado(s).%n", executed);
            }

        } catch (SQLException e) {
            System.err.println("[MIGRATOR] Error de conexión: " + e.getMessage());
            System.err.println("[MIGRATOR]   SQLState: " + e.getSQLState());
            System.exit(1);
        }
    }

    private static void createHistoryTable(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_HISTORY_TABLE);
        }
    }

    private static File[] discoverSqlFiles(File sqlDir) {
        File[] files = sqlDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".sql"));
        if (files == null) files = new File[0];
        Arrays.sort(files, Comparator.comparing(File::getName));
        return files;
    }

    private static Map<String, String> loadHistory(Connection conn) throws SQLException {
        Map<String, String> history = new LinkedHashMap<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery("SELECT filename, checksum FROM migration_history ORDER BY id")) {
            while (rs.next()) {
                history.put(rs.getString("filename"), rs.getString("checksum"));
            }
        }
        return history;
    }

    private static void executeSqlFile(Connection conn, String filename, String content)
            throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // execute() acepta scripts completos con múltiples statements y bloques $$ ... $$
            stmt.execute(content);
        } catch (SQLException e) {
            System.err.printf("[MIGRATOR] ✗ Error ejecutando %s%n", filename);
            System.err.printf("[MIGRATOR]   SQLState: %s%n", e.getSQLState());
            System.err.printf("[MIGRATOR]   Mensaje:  %s%n", e.getMessage());
            System.exit(1);
        }
    }

    private static void recordInHistory(Connection conn, String filename, String checksum)
            throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO migration_history (filename, checksum) VALUES (?, ?)")) {
            ps.setString(1, filename);
            ps.setString(2, checksum);
            ps.executeUpdate();
        }
    }

    private static void warnIfChecksumChanged(String filename, String current, String saved) {
        if (!current.equals(saved)) {
            System.out.printf(
                "[MIGRATOR] ⚠ Advertencia: %s fue modificado desde la última migración " +
                "(checksum no coincide). El archivo NO será re-ejecutado.%n", filename);
        }
    }

    private static String sha256(String content) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash);
    }
}
