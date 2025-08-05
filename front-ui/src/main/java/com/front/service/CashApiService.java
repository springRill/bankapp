package com.front.service;

import com.front.dto.CashDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CashApiService {

    RestClient cashServiceClient;

    public CashApiService() {
        cashServiceClient = RestClient.create("http://localhost:8082");
    }

    public void cash(CashDto cashDto) {
            cashServiceClient.post()
                    .uri("/cash")
                    .body(cashDto)
                    .retrieve()
                    .toBodilessEntity();
    }

}
