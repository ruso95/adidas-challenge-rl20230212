package com.adidas.backend.publicservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * <p>Configuration class for the webclient</p>
 *
 * @author <a href="mailto:andriyharyuk_95@hotmail.com">
 */
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
