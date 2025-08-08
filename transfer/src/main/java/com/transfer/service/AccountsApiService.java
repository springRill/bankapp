package com.transfer.service;

import com.transfer.dto.TransferDto;
import com.transfer.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import javax.management.OperationsException;

@Service
public class AccountsApiService {

    private final RestClient accountsServiceClient;

    public AccountsApiService(RestClient.Builder builder) {
        //accountsServiceClient = RestClient.create("http://localhost:8081/api");
        this.accountsServiceClient = builder.baseUrl("http://accounts-api/api").build();
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
