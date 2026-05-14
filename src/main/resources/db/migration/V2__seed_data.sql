--  IMPORTANTE: Ejecutar DESPUÉS del script DDL
-- ============================================================

-- ============================================================
--  1. GÉNEROS MUSICALES (10 registros)
-- ============================================================
INSERT INTO genero (nombre, descripcion) VALUES
    ('Rock',            'Música rock en todas sus variantes'),
    ('Pop',             'Música popular contemporánea'),
    ('Hip-Hop',         'Rap y cultura hip-hop urbana'),
    ('Electrónica',     'Música electrónica y dance'),
    ('Jazz',            'Jazz clásico y contemporáneo'),
    ('Reggaeton',       'Ritmo urbano latino'),
    ('Clásica',         'Música clásica occidental'),
    ('R&B',             'Rhythm and Blues'),
    ('Metal',           'Metal y sus subgéneros'),
    ('Salsa',           'Música tropical y salsa');


-- ============================================================
--  2. ARTISTAS (10 registros)
-- ============================================================
INSERT INTO artista (nom_artistico, pais, biografia) VALUES
    ('Los Oscuros',         'Colombia',     'Banda bogotana de rock alternativo formada en 2010.'),
    ('Valeria Reyes',       'México',       'Cantautora de pop latino con influencias del R&B.'),
    ('DJ Nexus',            'Argentina',    'Productor y DJ de música electrónica y house.'),
    ('The Midnight Crew',   'Estados Unidos','Cuarteto de jazz experimental con raíces en el blues.'),
    ('El Barrio',           'Colombia',     'Grupo de reggaeton y música urbana de Medellín.'),
    ('Orquesta Dorada',     'Cuba',         'Orquesta de salsa y música tropical con 30 años de trayectoria.'),
    ('Sombra Negra',        'Chile',        'Banda de metal progresivo con letras en español.'),
    ('Lena Russo',          'Italia',       'Soprano y compositora de música clásica contemporánea.'),
    ('FreakBeat',           'Colombia',     'Colectivo de hip-hop colombiano con mensaje social.'),
    ('Marcos Silva',        'Brasil',       'Guitarrista y compositor de pop acústico y bossa nova.');


-- ============================================================
--  3. ÁLBUMES (15 registros)
-- ============================================================
INSERT INTO album (id_artista, titulo, tipo, fecha_lanzamiento) VALUES
    (1, 'Noche Eterna',         'album',  '2022-03-15'),
    (1, 'Caos y Orden',         'ep',     '2024-01-20'),
    (2, 'Entre Luces',          'album',  '2023-07-04'),
    (2, 'Despertar',            'single', '2025-02-14'),
    (3, 'Pulsos',               'album',  '2023-11-01'),
    (4, 'Blue Hour Sessions',   'album',  '2022-09-10'),
    (5, 'Barrio Alto',          'album',  '2024-04-18'),
    (5, 'Fuego',                'single', '2025-01-05'),
    (6, 'La Dorada Suena',      'album',  '2021-06-21'),
    (7, 'Fractura',             'album',  '2023-05-30'),
    (8, 'Aria',                 'album',  '2022-12-01'),
    (9, 'Concreto y Sueños',    'album',  '2024-08-15'),
    (10,'Raíces',               'album',  '2023-03-22'),
    (3, 'Drop Zone',            'ep',     '2024-06-10'),
    (4, 'After Hours',          'single', '2025-03-01');


-- ============================================================
--  4. CANCIONES (30 registros)
-- ============================================================
INSERT INTO cancion (id_album, id_genero, titulo, duracion_seg, numero_pista) VALUES
-- Noche Eterna (album 1, Rock)
    (1,  1, 'Oscuridad',            214, 1),
    (1,  1, 'Sin Retorno',          198, 2),
    (1,  1, 'El Último Tren',       241, 3),
    (1,  1, 'Tormenta Interior',    187, 4),
-- Caos y Orden EP (album 2, Rock)
    (2,  1, 'Caos',                 173, 1),
    (2,  9, 'Orden Metal',          205, 2),
-- Entre Luces (album 3, Pop)
    (3,  2, 'Luz de Día',           193, 1),
    (3,  2, 'Nube Rosa',            211, 2),
    (3,  8, 'Feeling Good',         224, 3),
    (3,  2, 'Primavera',            178, 4),
-- Despertar single (album 4, Pop)
    (4,  2, 'Despertar',            195, 1),
-- Pulsos (album 5, Electrónica)
    (5,  4, 'Pulso Alpha',          342, 1),
    (5,  4, 'Drifting',             298, 2),
    (5,  4, 'Neon Rain',            315, 3),
-- Blue Hour Sessions (album 6, Jazz)
    (6,  5, 'Blue Monday',          387, 1),
    (6,  5, 'Sax at Midnight',      412, 2),
    (6,  5, 'The Last Note',        356, 3),
-- Barrio Alto (album 7, Reggaeton)
    (7,  6, 'Calor de Barrio',      198, 1),
    (7,  6, 'La Calle Llama',       212, 2),
    (7,  6, 'Noche de Viernes',     187, 3),
-- La Dorada Suena (album 9, Salsa)
    (9, 10, 'Sabrosura',            264, 1),
    (9, 10, 'Paso a Paso',          248, 2),
-- Fractura (album 10, Metal)
    (10, 9, 'Fractura',             287, 1),
    (10, 9, 'Abyss',                312, 2),
-- Concreto y Sueños (album 12, Hip-Hop)
    (12, 3, 'Concreto',             214, 1),
    (12, 3, 'Barrio Libre',         198, 2),
    (12, 3, 'La Verdad',            223, 3),
-- Raíces (album 13, Pop)
    (13, 2, 'Raíces',               201, 1),
    (13, 2, 'Bossa Tarde',          234, 2),
-- Drop Zone EP (album 14, Electrónica)
    (14, 4, 'Drop Zone',            328, 1);


-- ============================================================
--  5. ARTISTA_GENERO (géneros por artista)
-- ============================================================
INSERT INTO artista_genero (id_artista, id_genero) VALUES
    (1, 1),   -- Los Oscuros: Rock
    (1, 9),   -- Los Oscuros: Metal
    (2, 2),   -- Valeria Reyes: Pop
    (2, 8),   -- Valeria Reyes: R&B
    (3, 4),   -- DJ Nexus: Electrónica
    (4, 5),   -- The Midnight Crew: Jazz
    (4, 8),   -- The Midnight Crew: R&B
    (5, 6),   -- El Barrio: Reggaeton
    (5, 3),   -- El Barrio: Hip-Hop
    (6, 10),  -- Orquesta Dorada: Salsa
    (7, 9),   -- Sombra Negra: Metal
    (7, 1),   -- Sombra Negra: Rock
    (8, 7),   -- Lena Russo: Clásica
    (9, 3),   -- FreakBeat: Hip-Hop
    (10, 2),  -- Marcos Silva: Pop
    (10, 5);  -- Marcos Silva: Jazz


-- ============================================================
--  6. USUARIOS (10 registros, via stored procedure)
--     El SP crea automáticamente la suscripción freemium
-- ============================================================
CALL sp_registrar_usuario('Ricardo Carrero',   'ricardo@spoticlone.com',   '$2b$12$abc123hashricardo');
CALL sp_registrar_usuario('Anthony Vega',      'anthony@spoticlone.com',   '$2b$12$abc123hashanthony');
CALL sp_registrar_usuario('Samuel Mesa',       'samuel@spoticlone.com',    '$2b$12$abc123hashsamuel');
CALL sp_registrar_usuario('Laura Martínez',    'laura@spoticlone.com',     '$2b$12$abc123hashlauramt');
CALL sp_registrar_usuario('Andrés Torres',     'andres@spoticlone.com',    '$2b$12$abc123hashandrest');
CALL sp_registrar_usuario('Sofía Gómez',       'sofia@spoticlone.com',     '$2b$12$abc123hashsofiagm');
CALL sp_registrar_usuario('Camila Ruiz',       'camila@spoticlone.com',    '$2b$12$abc123hashcamilar');
CALL sp_registrar_usuario('Diego Herrera',     'diego@spoticlone.com',     '$2b$12$abc123hashdiegohm');
CALL sp_registrar_usuario('Valentina Cruz',    'valentina@spoticlone.com', '$2b$12$abc123hashvalenti');
CALL sp_registrar_usuario('Admin SpotiClone',  'admin@spoticlone.com',     '$2b$12$abc123hashadminsc');


-- ============================================================
--  7. SUSCRIPCIONES PREMIUM (usuarios 1, 2, 3 y 4)
--     Cambia de freemium a premium via stored procedure
-- ============================================================
CALL sp_cambiar_suscripcion(1, 'premium');
CALL sp_cambiar_suscripcion(2, 'premium');
CALL sp_cambiar_suscripcion(3, 'premium');
CALL sp_cambiar_suscripcion(4, 'premium');


-- ============================================================
--  8. PLAYLISTS (via stored procedure)
-- ============================================================
CALL sp_crear_playlist(1, 'Mis Favoritas de Rock',  'Lo mejor del rock en español',     TRUE);
CALL sp_crear_playlist(1, 'Para Estudiar',           'Música instrumental y electrónica', FALSE);
CALL sp_crear_playlist(2, 'Vibes Urbanos',           'Reggaeton y Hip-Hop del momento',   TRUE);
CALL sp_crear_playlist(3, 'Jazz & Soul',             'Sesiones de jazz y R&B',            TRUE);
CALL sp_crear_playlist(4, 'Mix Latino',              'Pop y salsa latina',                TRUE);
CALL sp_crear_playlist(5, 'Workout',                 'Energía para el gym',               TRUE);
CALL sp_crear_playlist(6, 'Noche de Viernes',        'Para la noche del fin de semana',   FALSE);
CALL sp_crear_playlist(7, 'Acústica',                'Solo guitarra y voz',               TRUE);


-- ============================================================
--  9. CANCIONES EN PLAYLISTS (via stored procedure)
--     El trigger actualiza total_canciones automáticamente
-- ============================================================

-- Playlist 1: Mis Favoritas de Rock (usuario 1)
CALL sp_agregar_cancion_playlist(1, 1);   -- Oscuridad
CALL sp_agregar_cancion_playlist(1, 2);   -- Sin Retorno
CALL sp_agregar_cancion_playlist(1, 3);   -- El Último Tren
CALL sp_agregar_cancion_playlist(1, 5);   -- Caos
CALL sp_agregar_cancion_playlist(1, 23);  -- Fractura

-- Playlist 2: Para Estudiar (usuario 1)
CALL sp_agregar_cancion_playlist(2, 12);  -- Pulso Alpha
CALL sp_agregar_cancion_playlist(2, 13);  -- Drifting
CALL sp_agregar_cancion_playlist(2, 14);  -- Neon Rain
CALL sp_agregar_cancion_playlist(2, 15);  -- Blue Monday
CALL sp_agregar_cancion_playlist(2, 30);  -- Drop Zone

-- Playlist 3: Vibes Urbanos (usuario 2)
CALL sp_agregar_cancion_playlist(3, 18);  -- Calor de Barrio
CALL sp_agregar_cancion_playlist(3, 19);  -- La Calle Llama
CALL sp_agregar_cancion_playlist(3, 25);  -- Concreto
CALL sp_agregar_cancion_playlist(3, 26);  -- Barrio Libre

-- Playlist 4: Jazz & Soul (usuario 3)
CALL sp_agregar_cancion_playlist(4, 15);  -- Blue Monday
CALL sp_agregar_cancion_playlist(4, 16);  -- Sax at Midnight
CALL sp_agregar_cancion_playlist(4, 17);  -- The Last Note
CALL sp_agregar_cancion_playlist(4, 9);   -- Feeling Good

-- Playlist 5: Mix Latino (usuario 4)
CALL sp_agregar_cancion_playlist(5, 7);   -- Luz de Día
CALL sp_agregar_cancion_playlist(5, 11);  -- Despertar
CALL sp_agregar_cancion_playlist(5, 21);  -- Sabrosura
CALL sp_agregar_cancion_playlist(5, 22);  -- Paso a Paso

-- Playlist 6: Workout (usuario 5)
CALL sp_agregar_cancion_playlist(6, 12);  -- Pulso Alpha
CALL sp_agregar_cancion_playlist(6, 18);  -- Calor de Barrio
CALL sp_agregar_cancion_playlist(6, 20);  -- Noche de Viernes
CALL sp_agregar_cancion_playlist(6, 30);  -- Drop Zone


-- ============================================================
--  10. LIKES / FAVORITOS
-- ============================================================
INSERT INTO like_cancion (id_usuario, id_cancion) VALUES
    (1,  1), (1,  2), (1,  3), (1,  7), (1, 12),
    (2, 18), (2, 19), (2, 25), (2, 26), (2, 20),
    (3, 15), (3, 16), (3, 17), (3,  9), (3,  8),
    (4,  7), (4, 11), (4, 21), (4, 22), (4, 10),
    (5, 12), (5, 14), (5, 30), (5, 18), (5,  5),
    (6,  7), (6,  8), (6,  9), (6, 11), (6, 28),
    (7,  1), (7,  3), (7, 23), (7, 24), (7,  6),
    (8, 15), (8, 16), (8, 21), (8, 22), (8, 29),
    (9, 25), (9, 26), (9, 27), (9, 18), (9, 12),
    (10, 1), (10, 7), (10,15), (10,21), (10,28);


-- ============================================================
--  11. VERIFICACIONES POST-INSERCIÓN
-- ============================================================

-- Contar registros por tabla
SELECT 'genero'           AS tabla, COUNT(*) AS registros FROM genero
UNION ALL
SELECT 'artista',                   COUNT(*)               FROM artista
UNION ALL
SELECT 'album',                     COUNT(*)               FROM album
UNION ALL
SELECT 'cancion',                   COUNT(*)               FROM cancion
UNION ALL
SELECT 'artista_genero',            COUNT(*)               FROM artista_genero
UNION ALL
SELECT 'usuario',                   COUNT(*)               FROM usuario
UNION ALL
SELECT 'suscripcion',               COUNT(*)               FROM suscripcion
UNION ALL
SELECT 'playlist',                  COUNT(*)               FROM playlist
UNION ALL
SELECT 'playlist_cancion',          COUNT(*)               FROM playlist_cancion
UNION ALL
SELECT 'like_cancion',              COUNT(*)               FROM like_cancion
UNION ALL
SELECT 'auditoria_log',             COUNT(*)               FROM auditoria_log
ORDER BY tabla;

-- Verificar que el trigger actualizó correctamente los contadores
SELECT p.id_playlist, p.nombre, p.total_canciones,
       COUNT(pc.id_cancion) AS canciones_reales
FROM   playlist p
LEFT JOIN playlist_cancion pc ON pc.id_playlist = p.id_playlist
GROUP  BY p.id_playlist, p.nombre, p.total_canciones
ORDER  BY p.id_playlist;

-- Verificar suscripciones premium
SELECT u.nombre, s.tipo, s.fecha_inicio, s.fecha_fin, s.precio
FROM   suscripcion s
JOIN   usuario u ON u.id_usuario = s.id_usuario
ORDER  BY s.tipo DESC, u.nombre;

-- Probar funciones
SELECT fn_duracion_total_playlist(1) AS duracion_playlist1_seg;
SELECT fn_canciones_por_artista(1)   AS canciones_los_oscuros;
SELECT fn_tiene_suscripcion_activa(1) AS usuario1_premium;
SELECT fn_tiene_suscripcion_activa(5) AS usuario5_premium;

-- ============================================================
--  FIN DEL SCRIPT DML
--  SpotiClone – Universidad El Bosque – 2026-1
-- ============================================================