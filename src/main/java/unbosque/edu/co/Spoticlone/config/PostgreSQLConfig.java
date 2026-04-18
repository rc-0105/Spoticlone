package unbosque.edu.co.Spoticlone.config;

import org.springframework.context.annotation.Configuration;

/**
 * La DataSource y el JdbcTemplate son auto-configurados por Spring Boot
 * a partir de las propiedades spring.datasource.* en application.properties.
 * <p>
 * Este proyecto usa JDBC puro:
 *   - JdbcTemplate  → queries SELECT simples
 *   - CallableStatement vía dataSource.getConnection() → stored procedures y funciones
 * <p>
 * JPA / Hibernate están excluidos de la auto-configuración en application.properties.
 */
@Configuration
public class PostgreSQLConfig {
    // No se necesita declarar beans manuales.
    // DataSource y JdbcTemplate son provistos por spring-boot-starter-jdbc.
}
