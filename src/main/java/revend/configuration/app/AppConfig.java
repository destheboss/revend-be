package revend.configuration.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import revend.configuration.security.SecretKeyGenerator;

@Configuration
public class AppConfig {
    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Value("${jwt.secret}")
    private String secretKey;

    @PostConstruct
    public void init() {
        secretKey = SecretKeyGenerator.generateSecretKey();
        logger.info("Generated Secret Key: {}", secretKey);
    }
}
