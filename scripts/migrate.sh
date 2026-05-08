#!/bin/bash
# ============================================================
#  migrate.sh – Ejecuta las migraciones de SpotiClone
#  Uso: ./scripts/migrate.sh <password>
#  Debe ejecutarse desde la RAÍZ del proyecto Maven.
# ============================================================

set -e  # salir si cualquier comando falla

# ── Configuración ─────────────────────────────────────────────
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_NAME="${DB_NAME:-spoticlone}"
DB_USER="${DB_USER:-spoticlone_app}"

# El password se puede pasar como argumento $1 o como variable de entorno
if [ -n "$1" ]; then
    DB_PASSWORD="$1"
elif [ -z "$DB_PASSWORD" ]; then
    echo "[MIGRATOR] Error: se requiere el password."
    echo "           Uso: ./scripts/migrate.sh <password>"
    echo "           O:   export DB_PASSWORD=<password> && ./scripts/migrate.sh"
    exit 1
fi

# ── Exportar variables para el Migrator ───────────────────────
export DB_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}"
export DB_USER
export DB_PASSWORD
export SQL_DIR="sql"

# ── Verificar que estamos en la raíz del proyecto ─────────────
if [ ! -f "pom.xml" ]; then
    echo "[MIGRATOR] Error: este script debe ejecutarse desde la raíz del proyecto Maven."
    echo "           Directorio actual: $(pwd)"
    exit 1
fi

echo "[MIGRATOR] Conectando a: ${DB_URL}"
echo "[MIGRATOR] Usuario:      ${DB_USER}"
echo "[MIGRATOR] Carpeta SQL:  ${SQL_DIR}/"
echo ""

# ── Ejecutar el Migrator via Maven ────────────────────────────
mvn -q exec:java -Dexec.mainClass=unbosque.edu.co.Spoticlone.migrate.Migrator
