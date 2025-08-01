package com.front.service;

import com.front.dto.AccountDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AccountsApiService {

    RestClient accountsServiceClient;

    public AccountsApiService() {
        accountsServiceClient = RestClient.create("http://localhost:8081");
    }

    public AccountDto getAccountByName(String username) {
        AccountDto accountDto = accountsServiceClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/account")
                        .queryParam("username", username)
                        .build())
                .retrieve()
                .body(AccountDto.class);
        return accountDto;
    }

    public AccountDto createAccount(AccountDto accountDto) {
        accountDto = accountsServiceClient.post()
                .uri("/account")
                .body(accountDto)
                .retrieve()
                .body(AccountDto.class);
        return accountDto;
    }


}
