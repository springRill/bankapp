package com.cash.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class BlockerApiService {

    private final RestClient blockerServiceClient;

    public BlockerApiService(RestClient.Builder builder) {
        //blockerServiceClient = RestClient.create("http://localhost:8087/api");
        this.blockerServiceClient = builder.baseUrl("http://blocker-api/api").build();
    }

    public Boolean validate(){
        return blockerServiceClient.get()
                .uri("/validate")
                .retrieve()
                .body(Boolean.class);
    }
}
