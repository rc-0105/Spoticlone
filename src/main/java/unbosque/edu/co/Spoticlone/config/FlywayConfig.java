package unbosque.edu.co.Spoticlone.config;

import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    private static final Logger log = LoggerFactory.getLogger(FlywayConfig.class);

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            try {
                flyway.migrate();
            } catch (FlywayException ex) {
                for (MigrationInfo info : flyway.info().all()) {
                    if (info.getState() == MigrationState.FAILED) {
                        log.error("=== FLYWAY MIGRATION FAILED ===");
                        log.error("  Version     : {}", info.getVersion());
                        log.error("  Description : {}", info.getDescription());
                        log.error("  Script      : {}", info.getScript());
                        log.error("  State       : {}", info.getState());
                        log.error("  Error       : {}", ex.getMessage());
                        log.error("================================");
                    }
                }
                throw ex;
            }
        };
    }
}
