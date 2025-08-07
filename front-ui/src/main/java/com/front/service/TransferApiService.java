package com.front.service;

import com.front.dto.ExchangeDto;
import com.front.dto.TransferDto;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class TransferApiService {

    private final RestClient transferServiceClient;

    public TransferApiService(DiscoveryClient discoveryClient) {
        List<ServiceInstance> instances = discoveryClient.getInstances("transfer-api");
        if (instances.isEmpty()) {
            //transferServiceClient = RestClient.create("http://localhost:8083");
            throw new IllegalStateException("Service 'transfer-api' not found in DiscoveryClient");
        } else {
            ServiceInstance instance = instances.get(0);
            transferServiceClient = RestClient.create(instance.getUri().toString());
        }
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
