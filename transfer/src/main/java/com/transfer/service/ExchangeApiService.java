package com.transfer.service;

import com.transfer.dto.CurrencyEnum;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ExchangeApiService {

    private final RestClient exchangeServiceClient;

    public ExchangeApiService(RestClient.Builder builder) {
        //exchangeServiceClient = RestClient.create("http://localhost:8084/api");
        this.exchangeServiceClient = builder.baseUrl("http://exchange-api/api").build();
    }

    public Double getExchangeValue(CurrencyEnum currencyEnum) {
        return exchangeServiceClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/exchange/{currency}")
                        .build(currencyEnum.name()))
                .retrieve()
                .body(Double.class);
    }

}
