package com.front.service;

import com.front.dto.CashDto;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class CashApiService {

    private final RestClient cashServiceClient;

    public CashApiService(DiscoveryClient discoveryClient) {
        List<ServiceInstance> instances = discoveryClient.getInstances("cash-api");
        if (instances.isEmpty()) {
            //cashServiceClient = RestClient.create("http://localhost:8082");
            throw new IllegalStateException("Service 'cash-api' not found in DiscoveryClient");
        } else {
            ServiceInstance instance = instances.get(0);
            cashServiceClient = RestClient.create(instance.getUri().toString());
        }
    }

    public void cash(CashDto cashDto) {
            cashServiceClient.post()
                    .uri("/cash")
                    .body(cashDto)
                    .retrieve()
                    .toBodilessEntity();
    }

}
