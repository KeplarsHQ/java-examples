package com.keplars.examples.config;

import com.keplars.KeplarsClient;
import com.keplars.KeplarsClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeplarsConfig {

    @Value("${keplars.api-key}")
    private String apiKey;

    @Value("${keplars.base-url:https://api.keplars.com}")
    private String baseUrl;

    @Bean
    public KeplarsClient keplarsClient() {
        return KeplarsClient.create(
            new KeplarsClientConfig(apiKey, baseUrl, 30_000L, 3, 1_000L)
        );
    }
}
