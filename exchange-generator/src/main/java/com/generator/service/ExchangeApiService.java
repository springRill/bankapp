package com.generator.service;

import com.generator.dto.ExchangeDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ExchangeApiService {

    private final RestClient exchangeServiceClient;

    public ExchangeApiService(RestClient.Builder builder) {
        //exchangeServiceClient = RestClient.create("http://localhost:8084/api");
        this.exchangeServiceClient = builder.baseUrl("http://exchange-api/api").build();
    }

    public void setExchange(ExchangeDto exchangeDto) {
        exchangeServiceClient.post()
                .uri("/exchange")
                .body(exchangeDto)
                .retrieve()
                .toBodilessEntity();
    }

}
