package com.transfer.service;

import com.transfer.dto.CurrencyEnum;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ExchangeApiService {

    private final RestClient exchangeServiceClient;

    public ExchangeApiService(DiscoveryClient discoveryClient) {
        List<ServiceInstance> instances = discoveryClient.getInstances("exchange-api");
        if (instances.isEmpty()) {
            //exchangeServiceClient = RestClient.create("http://localhost:8084");
            throw new IllegalStateException("Service 'exchange-api' not found in DiscoveryClient");
        } else {
            ServiceInstance instance = instances.get(0);
            exchangeServiceClient = RestClient.create(instance.getUri().toString());
        }
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
