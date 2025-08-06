package com.generator.service;

import com.generator.dto.ExchangeDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ExchangeApiService {

    RestClient exchangeServiceClient;

    public ExchangeApiService() {
        exchangeServiceClient = RestClient.create("http://localhost:8084");
    }

    public void setExchange(ExchangeDto exchangeDto) {
        exchangeServiceClient.post()
                .uri("/exchange")
                .body(exchangeDto)
                .retrieve()
                .toBodilessEntity();
    }

}
