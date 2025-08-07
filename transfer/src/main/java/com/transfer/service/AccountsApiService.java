package com.transfer.service;

import com.transfer.dto.TransferDto;
import com.transfer.dto.UserDto;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import javax.management.OperationsException;
import java.util.List;

@Service
public class AccountsApiService {

    private final RestClient accountsServiceClient;

    public AccountsApiService(DiscoveryClient discoveryClient) {
        List<ServiceInstance> instances = discoveryClient.getInstances("accounts-api");
        if (instances.isEmpty()) {
            //accountsServiceClient = RestClient.create("http://localhost:8081");
            throw new IllegalStateException("Service 'accounts-api' not found in DiscoveryClient");
        } else {
            ServiceInstance instance = instances.get(0);
            accountsServiceClient = RestClient.create(instance.getUri().toString());
        }
    }

    public void transfer(TransferDto transferDto) throws OperationsException {
        try {
            accountsServiceClient.post()
                    .uri("/account/transfer")
                    .body(transferDto)
                    .retrieve()
                    .toBodilessEntity();
        }catch (RestClientResponseException restClientResponseException){
            throw new OperationsException(restClientResponseException.getResponseBodyAsString());
        }
    }

    public UserDto getUserById(Long userId) {
        return accountsServiceClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/account/user/{userId}")
                        .build(userId))
                .retrieve()
                .body(UserDto.class);
    }

}
