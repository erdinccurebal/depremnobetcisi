package com.depremnobetcisi.infrastructure.config;

import com.depremnobetcisi.domain.port.output.EarthquakeApiClient;
import com.depremnobetcisi.infrastructure.output.api.KandilliApiClientAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${app.kandilli.base-url:https://api.orhanaydogdu.com.tr}")
    private String baseUrl;

    @Bean
    public RestClient kandilliRestClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public EarthquakeApiClient earthquakeApiClient(RestClient kandilliRestClient, ObjectMapper objectMapper) {
        return new KandilliApiClientAdapter(kandilliRestClient, objectMapper);
    }
}
