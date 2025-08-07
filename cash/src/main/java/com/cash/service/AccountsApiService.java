package com.cash.service;

import com.cash.dto.CashDto;
import com.cash.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import javax.management.OperationsException;

@Service
public class AccountsApiService {

    private final RestClient accountsServiceClient;

    public AccountsApiService() {
        accountsServiceClient = RestClient.create("http://localhost:8081");
    }

    public void cash(CashDto cashDto) throws OperationsException {
        try {
            accountsServiceClient.post()
                    .uri("/account/cash")
                    .body(cashDto)
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
