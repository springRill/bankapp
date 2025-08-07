package com.cash.service;

import com.cash.dto.UserDto;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class BlockerApiService {

    private final RestClient blockerServiceClient;

    public BlockerApiService(DiscoveryClient discoveryClient) {
        List<ServiceInstance> instances = discoveryClient.getInstances("blocker-api");
        if (instances.isEmpty()) {
            //blockerServiceClient = RestClient.create("http://localhost:8087");
            throw new IllegalStateException("Service 'blocker-api' not found in DiscoveryClient");
        } else {
            ServiceInstance instance = instances.get(0);
            blockerServiceClient = RestClient.create(instance.getUri().toString());
        }
    }

    public Boolean validate(){
        return blockerServiceClient.get()
                .uri("/validate")
                .retrieve()
                .body(Boolean.class);
    }
}
