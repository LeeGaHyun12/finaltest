package hyun.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApiConfig {

    @Value("${naver.tts.client-id}")
    private String clientId;

    @Value("${naver.tts.client-secret}")
    private String clientSecret;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public String clientId() {
        return clientId;
    }

    @Bean
    public String clientSecret() {
        return clientSecret;
    }
}
