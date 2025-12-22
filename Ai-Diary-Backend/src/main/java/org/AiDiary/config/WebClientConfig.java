package org.AiDiary.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient creteWebClient(@Value("${mlService.api.key}") String apiKey,
                                    @Value("${mlService.api.url}") String bUrl){
        return null;

    }
}
