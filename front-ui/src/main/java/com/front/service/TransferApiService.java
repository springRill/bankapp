package com.front.service;

import com.front.configuration.SecurityConfiguration;
import com.front.dto.ExchangeDto;
import com.front.dto.TransferDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class TransferApiService {

/*
    private final RestClient transferServiceClient;

//    public TransferApiService(RestClient.Builder builder, @Value("${appservices.transfer-api:http://transfer-api/api}") String baseUrl) {
    public TransferApiService(RestClient.Builder builder, @Value("${appservices.transfer-api:http://localhost:8083/api}") String baseUrl) {
        this.transferServiceClient = builder.baseUrl(baseUrl).build();
    }
*/

/*
    public TransferApiService(RestClient.Builder builder, SecurityConfiguration.OAuth2AuthInterceptor authInterceptor, @Value("${appservices.transfer-api:http://localhost:8083/api}") String baseUrl) {
        this.transferServiceClient = builder.baseUrl(baseUrl).requestInterceptor(authInterceptor).build();
    }
*/

    private final RestClient transferServiceClient;

    public TransferApiService(@Qualifier("transferApiClient") RestClient transferApiClient) {
        this.transferServiceClient = transferApiClient;
    }

    @CircuitBreaker(name = "cbservice")
    public void transfer(TransferDto transferDto) {
        transferServiceClient.post()
                .uri("/transfer")
                .body(transferDto)
                .retrieve()
                .toBodilessEntity();
    }

    @CircuitBreaker(name = "cbservice")
    public List<ExchangeDto> getExchangeDtoList() {
        return transferServiceClient.get()
                .uri("/transfer")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ExchangeDto>>() {});
    }
}
