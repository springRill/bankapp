package com.front.service;

import com.front.dto.ExchangeDto;
import com.front.dto.TransferDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class TransferApiService {

    private final RestClient transferServiceClient;

    public TransferApiService(RestClient.Builder builder) {
        //transferServiceClient = RestClient.create("http://localhost:8083/api");
        this.transferServiceClient = builder.baseUrl("http://transfer-api/api").build();
    }

    public void transfer(TransferDto transferDto) {
        transferServiceClient.post()
                .uri("/transfer")
                .body(transferDto)
                .retrieve()
                .toBodilessEntity();
    }

    public List<ExchangeDto> getExchangeDtoList() {
        return transferServiceClient.get()
                .uri("/transfer")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ExchangeDto>>() {});
    }
}
