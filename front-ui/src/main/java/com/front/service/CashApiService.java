package com.front.service;

import com.front.dto.CashDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CashApiService {

    private final RestClient cashServiceClient;

    public CashApiService(RestClient.Builder builder) {
        //cashServiceClient = RestClient.create("http://localhost:8082/api");
        this.cashServiceClient = builder.baseUrl("http://cash-api/api").build();
    }

    public void cash(CashDto cashDto) {
        cashServiceClient.post()
                .uri("/cash")
                .body(cashDto)
                .retrieve()
                .toBodilessEntity();
    }

}
