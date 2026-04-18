package unbosque.edu.co.Spoticlone.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configura el MongoClient nativo (mongodb-driver-sync).
 * NO usa Spring Data MongoDB ni MongoTemplate.
 */
@Configuration
public class MongoConfig {

    @Value("${spoticlone.mongodb.uri}")
    private String mongoUri;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }
}
