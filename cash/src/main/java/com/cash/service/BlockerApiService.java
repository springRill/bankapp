package com.cash.service;

import com.cash.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class BlockerApiService {

    private final RestClient blockerServiceClient;

    public BlockerApiService() {
        this.blockerServiceClient = RestClient.create("http://localhost:8087");;
    }

    public Boolean validate(){
        return blockerServiceClient.get()
                .uri("/validate")
                .retrieve()
                .body(Boolean.class);
    }
}
