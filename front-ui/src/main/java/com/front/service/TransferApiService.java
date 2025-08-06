package com.front.service;

import com.front.dto.TransferDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TransferApiService {

    RestClient transferServiceClient;

    public TransferApiService() {
        transferServiceClient = RestClient.create("http://localhost:8083");
    }

    public void transfer(TransferDto transferDto) {
        transferServiceClient.post()
                .uri("/transfer")
                .body(transferDto)
                .retrieve()
                .toBodilessEntity();
    }
}
