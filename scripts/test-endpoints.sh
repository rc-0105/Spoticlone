#!/bin/bash
# ============================================================
#  SpotiClone — API endpoint test suite
#  Usage: bash scripts/test-endpoints.sh [BASE_URL]
#  Default BASE_URL: http://localhost:8080
# ============================================================

BASE="${1:-http://localhost:8080}"
PASS=0
FAIL=0

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m'

section() { echo -e "\n${CYAN}── $1 ──${NC}"; }
pass()    { echo -e "  ${GREEN}PASS${NC}  $1"; PASS=$((PASS+1)); }
fail()    { echo -e "  ${RED}FAIL${NC}  $1 — esperado $2, got $3"; FAIL=$((FAIL+1)); }
info()    { echo -e "  ${YELLOW}INFO${NC}  $1"; }

# Hace un request y verifica el HTTP status code
# check LABEL METHOD URL [BODY] [HEADERS...] EXPECTED_STATUS
check() {
  local label=$1 method=$2 url=$3
  shift 3

  local body="" headers=() expected
  while [[ $# -gt 1 ]]; do
    if [[ "$1" == -H ]]; then
      headers+=(-H "$2"); shift 2
    elif [[ "$1" == -d ]]; then
      body=$2; shift 2
    else
      shift
    fi
  done
  expected=$1

  local args=(-s -o /dev/null -w "%{http_code}" -X "$method")
  [[ -n "$body" ]] && args+=(-H "Content-Type: application/json" -d "$body")
  args+=("${headers[@]}")

  local status
  status=$(curl "${args[@]}" "$url")

  if [[ "$status" == "$expected" ]]; then
    pass "$label"
  else
    fail "$label" "$expected" "$status"
  fi
}

# Como check pero también retorna el body completo
fetch() {
  local method=$1 url=$2
  shift 2
  local body="" headers=() args=(-s -X "$method")
  while [[ $# -gt 0 ]]; do
    if [[ "$1" == -H ]]; then
      headers+=(-H "$2"); shift 2
    elif [[ "$1" == -d ]]; then
      body=$2; shift 2
    else
      shift
    fi
  done
  [[ -n "$body" ]] && args+=(-H "Content-Type: application/json" -d "$body")
  args+=("${headers[@]}")
  curl "${args[@]}" "$url"
}

# Extrae un valor JSON sin jq
json_get() {
  echo "$1" | grep -o "\"$2\":\"[^\"]*\"" | head -1 | sed 's/.*":"\(.*\)"/\1/'
}
json_get_num() {
  echo "$1" | grep -o "\"$2\":[0-9]*" | head -1 | sed "s/\"$2\"://"
}

echo -e "${CYAN}============================================"
echo -e "  SpotiClone API Test Suite"
echo -e "  Target: $BASE"
echo -e "============================================${NC}"

# ─────────────────────────────────────────────────────────────
section "0. Salud — app corriendo"
# ─────────────────────────────────────────────────────────────
HEALTH=$(curl -s -o /dev/null -w "%{http_code}" "$BASE/api/catalog/generos" \
  -H "Authorization: Bearer dummy")
if [[ "$HEALTH" == "401" || "$HEALTH" == "200" ]]; then
  pass "App responde en $BASE"
else
  echo -e "  ${RED}ERROR${NC}  App no responde (status: $HEALTH). ¿Está corriendo?"
  exit 1
fi

# ─────────────────────────────────────────────────────────────
section "1. Auth — sin token"
# ─────────────────────────────────────────────────────────────
check "GET /api/catalog/generos sin token → 401" \
  GET "$BASE/api/catalog/generos" 401

check "POST /api/playlists sin token → 401" \
  GET "$BASE/api/playlists" 401

# ─────────────────────────────────────────────────────────────
section "2. Registro e inicio de sesión"
# ─────────────────────────────────────────────────────────────
TEST_EMAIL="test_$(date +%s)@spoticlone.com"
TEST_PASS="TestPass123!"

# Registro
REG_STATUS=$(curl -s -o /dev/null -w "%{http_code}" -X POST \
  -H "Content-Type: application/json" \
  -d "{\"nombre\":\"Test User\",\"email\":\"$TEST_EMAIL\",\"password\":\"$TEST_PASS\"}" \
  "$BASE/api/auth/register")

if [[ "$REG_STATUS" == "201" ]]; then
  pass "POST /api/auth/register → 201"
else
  fail "POST /api/auth/register" "201" "$REG_STATUS"
fi

# Login
LOGIN=$(fetch POST "$BASE/api/auth/login" \
  -d "{\"email\":\"$TEST_EMAIL\",\"password\":\"$TEST_PASS\"}")
TOKEN=$(json_get "$LOGIN" "token")

if [[ -n "$TOKEN" ]]; then
  pass "POST /api/auth/login → token obtenido"
  info "Token: ${TOKEN:0:40}..."
else
  echo -e "  ${RED}ERROR${NC}  No se pudo obtener token. Tests siguientes van a fallar."
  echo "  Response: $LOGIN"
  exit 1
fi

AUTH="-H Authorization: Bearer $TOKEN"

# Login con credenciales inválidas
check "POST /api/auth/login credenciales inválidas → 401" \
  POST "$BASE/api/auth/login" \
  -d '{"email":"noexiste@x.com","password":"wrong"}' \
  401

# Registro duplicado
check "POST /api/auth/register email duplicado → 409" \
  POST "$BASE/api/auth/register" \
  -d "{\"nombre\":\"Dup\",\"email\":\"$TEST_EMAIL\",\"password\":\"pass\"}" \
  409

# ─────────────────────────────────────────────────────────────
section "3. Catálogo"
# ─────────────────────────────────────────────────────────────
check "GET /api/catalog/generos → 200" \
  GET "$BASE/api/catalog/generos" \
  -H "Authorization: Bearer $TOKEN" 200

check "GET /api/catalog/artistas → 200" \
  GET "$BASE/api/catalog/artistas" \
  -H "Authorization: Bearer $TOKEN" 200

check "GET /api/catalog/artistas/1 → 200" \
  GET "$BASE/api/catalog/artistas/1" \
  -H "Authorization: Bearer $TOKEN" 200

check "GET /api/catalog/artistas/99999 (no existe) → 404" \
  GET "$BASE/api/catalog/artistas/99999" \
  -H "Authorization: Bearer $TOKEN" 404

check "GET /api/catalog/albumes → 200" \
  GET "$BASE/api/catalog/albumes" \
  -H "Authorization: Bearer $TOKEN" 200

check "GET /api/catalog/albumes/1/canciones → 200" \
  GET "$BASE/api/catalog/albumes/1/canciones" \
  -H "Authorization: Bearer $TOKEN" 200

check "GET /api/catalog/albumes/1 (detalle con canciones) → 200" \
  GET "$BASE/api/catalog/albumes/1" \
  -H "Authorization: Bearer $TOKEN" 200

check "GET /api/catalog/albumes/99999 (no existe) → 404" \
  GET "$BASE/api/catalog/albumes/99999" \
  -H "Authorization: Bearer $TOKEN" 404

check "GET /api/catalog/canciones → 200" \
  GET "$BASE/api/catalog/canciones" \
  -H "Authorization: Bearer $TOKEN" 200

check "GET /api/catalog/canciones?genero=Rock → 200" \
  GET "$BASE/api/catalog/canciones?genero=Rock" \
  -H "Authorization: Bearer $TOKEN" 200

check "GET /api/catalog/canciones?artista=Valeria → 200" \
  GET "$BASE/api/catalog/canciones?artista=Valeria" \
  -H "Authorization: Bearer $TOKEN" 200

# ─────────────────────────────────────────────────────────────
section "4. Usuarios"
# ─────────────────────────────────────────────────────────────

# Obtener el ID del usuario autenticado desde GET /me
ME_RESP=$(fetch GET "$BASE/api/usuarios/me" -H "Authorization: Bearer $TOKEN")
MY_USER_ID_FROM_ME=$(json_get_num "$ME_RESP" "idUsuario")
info "Perfil propio: id=$MY_USER_ID_FROM_ME"

check "GET /api/usuarios/me → 200" \
  GET "$BASE/api/usuarios/me" \
  -H "Authorization: Bearer $TOKEN" 200

check "GET /api/usuarios/1 → 200" \
  GET "$BASE/api/usuarios/1" \
  -H "Authorization: Bearer $TOKEN" 200

check "GET /api/usuarios (sin rol admin) → 403" \
  GET "$BASE/api/usuarios" \
  -H "Authorization: Bearer $TOKEN" 403

# ─────────────────────────────────────────────────────────────
section "5. Playlists"
# ─────────────────────────────────────────────────────────────

# Crear playlist
CREATE_PL_STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
  -X POST \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Mi Playlist Test","descripcion":"Test","esPublica":true}' \
  "$BASE/api/playlists")

if [[ "$CREATE_PL_STATUS" == "201" ]]; then
  pass "POST /api/playlists → 201"
else
  fail "POST /api/playlists" "201" "$CREATE_PL_STATUS"
fi

# Extraer el id_usuario del JWT (payload es el segundo segmento, base64url → base64 estándar)
JWT_PAYLOAD=$(echo "$TOKEN" | cut -d'.' -f2 | tr '_-' '/+' | base64 -d 2>/dev/null)
MY_USER_ID=$(echo "$JWT_PAYLOAD" | grep -o '"sub":"[0-9]*"' | grep -o '[0-9]*' | head -1)
# Fallback: usar MY_USER_ID_FROM_ME si el JWT decode falló
[[ -z "$MY_USER_ID" ]] && MY_USER_ID="$MY_USER_ID_FROM_ME"

# Obtener el ID de la playlist más reciente del usuario (la que acabamos de crear)
MY_PLAYLISTS=$(fetch GET "$BASE/api/playlists/usuario/$MY_USER_ID" -H "Authorization: Bearer $TOKEN")
MY_PL_ID=$(json_get_num "$MY_PLAYLISTS" "idPlaylist")
info "Playlist propia creada con id: $MY_PL_ID (usuario: $MY_USER_ID)"

# Validación: nombre vacío
check "POST /api/playlists sin nombre → 400" \
  POST "$BASE/api/playlists" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"nombre":"","esPublica":true}' \
  400

check "GET /api/playlists/publicas → 200" \
  GET "$BASE/api/playlists/publicas" \
  -H "Authorization: Bearer $TOKEN" 200

check "GET /api/playlists/1 → 200" \
  GET "$BASE/api/playlists/1" \
  -H "Authorization: Bearer $TOKEN" 200

check "GET /api/playlists/99999 (no existe) → 404" \
  GET "$BASE/api/playlists/99999" \
  -H "Authorization: Bearer $TOKEN" 404

# Agregar canción a la playlist propia (no a la ajena)
check "POST /api/playlists/$MY_PL_ID/canciones → 201" \
  POST "$BASE/api/playlists/$MY_PL_ID/canciones" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"idCancion":1}' \
  201

# Editar playlist propia
check "PUT /api/playlists/$MY_PL_ID (editar nombre) → 200" \
  PUT "$BASE/api/playlists/$MY_PL_ID" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"nombre":"Playlist Editada QA","descripcion":"Test de edicion","esPublica":true}' \
  200

# Crear una playlist adicional para eliminarla (no la que ya usamos)
DEL_PL_STATUS=$(curl -s -o /dev/null -w "%{http_code}" \
  -X POST \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Playlist Para Borrar","descripcion":"temporal","esPublica":false}' \
  "$BASE/api/playlists")

if [[ "$DEL_PL_STATUS" == "201" ]]; then
  pass "POST /api/playlists (para borrar) → 201"
  # Obtener id de la playlist más reciente (la recién creada)
  ALL_PLS=$(fetch GET "$BASE/api/playlists/usuario/$MY_USER_ID" -H "Authorization: Bearer $TOKEN")
  DEL_PL_ID=$(json_get_num "$ALL_PLS" "idPlaylist")
  info "Playlist temporal a eliminar: $DEL_PL_ID"

  # Eliminar
  check "DELETE /api/playlists/$DEL_PL_ID → 200" \
    DELETE "$BASE/api/playlists/$DEL_PL_ID" \
    -H "Authorization: Bearer $TOKEN" \
    200

  # Verificar que ya no existe
  check "GET /api/playlists/$DEL_PL_ID (tras DELETE) → 404" \
    GET "$BASE/api/playlists/$DEL_PL_ID" \
    -H "Authorization: Bearer $TOKEN" \
    404
else
  fail "POST /api/playlists (para borrar)" "201" "$DEL_PL_STATUS"
fi

# ─────────────────────────────────────────────────────────────
section "6. Reproducciones"
# ─────────────────────────────────────────────────────────────

REG_REPR=$(curl -s -o /dev/null -w "%{http_code}" \
  -X POST \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "idCancion": 1,
    "duracionTotalSeg": 214,
    "duracionEscuchadaSeg": 200,
    "completada": true,
    "dispositivo": "web",
    "contexto": {"tipo": "playlist", "idReferencia": 1}
  }' \
  "$BASE/api/reproducciones")

if [[ "$REG_REPR" == "201" ]]; then
  pass "POST /api/reproducciones → 201"
else
  fail "POST /api/reproducciones" "201" "$REG_REPR"
fi

# Reproducción con canción inexistente
check "POST /api/reproducciones idCancion inexistente → 404" \
  POST "$BASE/api/reproducciones" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "idCancion": 99999,
    "duracionTotalSeg": 200,
    "duracionEscuchadaSeg": 180,
    "completada": false,
    "dispositivo": "web",
    "contexto": {"tipo": "playlist", "idReferencia": 1}
  }' \
  404

check "GET /api/reproducciones/top → 200" \
  GET "$BASE/api/reproducciones/top" \
  -H "Authorization: Bearer $TOKEN" 200

# IDOR — intentar ver historial de usuario 1 con token de otro usuario
# Nuestro usuario registrado tendrá ID > 10 (seed tiene 10 users)
check "GET /api/reproducciones/historial/1 (IDOR) → 403" \
  GET "$BASE/api/reproducciones/historial/1" \
  -H "Authorization: Bearer $TOKEN" 403

check "GET /api/reproducciones/genero-favorito/1 (IDOR) → 403" \
  GET "$BASE/api/reproducciones/genero-favorito/1" \
  -H "Authorization: Bearer $TOKEN" 403

# ─────────────────────────────────────────────────────────────
section "7. Suscripciones"
# ─────────────────────────────────────────────────────────────

check "GET /api/suscripciones/1 → 200" \
  GET "$BASE/api/suscripciones/1" \
  -H "Authorization: Bearer $TOKEN" 200

check "GET /api/suscripciones/99999 (no existe) → 404" \
  GET "$BASE/api/suscripciones/99999" \
  -H "Authorization: Bearer $TOKEN" 404

check "PUT /api/suscripciones/1 → 200" \
  PUT "$BASE/api/suscripciones/1" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"nuevoTipo":"premium"}' \
  200

check "PUT /api/suscripciones/1 tipo inválido → 400" \
  PUT "$BASE/api/suscripciones/1" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"nuevoTipo":"vip_ultra"}' \
  400

# ─────────────────────────────────────────────────────────────
section "8. Validaciones de request"
# ─────────────────────────────────────────────────────────────

check "POST /api/auth/register sin email → 400" \
  POST "$BASE/api/auth/register" \
  -d '{"nombre":"X","password":"pass"}' \
  400

check "POST /api/auth/login sin password → 400" \
  POST "$BASE/api/auth/login" \
  -d '{"email":"x@x.com"}' \
  400

check "POST /api/reproducciones dispositivo inválido → 400" \
  POST "$BASE/api/reproducciones" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "idCancion": 1,
    "duracionTotalSeg": 200,
    "duracionEscuchadaSeg": 100,
    "completada": false,
    "dispositivo": "smartfridge",
    "contexto": {"tipo": "playlist", "idReferencia": 1}
  }' \
  400

# ─────────────────────────────────────────────────────────────
echo ""
echo -e "${CYAN}════════════════════════════════════════${NC}"
TOTAL=$((PASS+FAIL))
echo -e "  Total: $TOTAL  |  ${GREEN}PASS: $PASS${NC}  |  ${RED}FAIL: $FAIL${NC}"
echo -e "${CYAN}════════════════════════════════════════${NC}"

[[ $FAIL -eq 0 ]] && exit 0 || exit 1
