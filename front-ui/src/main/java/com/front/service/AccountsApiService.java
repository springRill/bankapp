package com.front.service;

import com.front.dto.AccountDto;
import com.front.dto.CurrencyEnum;
import com.front.dto.UserDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class AccountsApiService {

    RestClient accountsServiceClient;

    public AccountsApiService() {
        accountsServiceClient = RestClient.create("http://localhost:8081");
    }

    public List<UserDto> getUsers() {
        return accountsServiceClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/account/users")
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<UserDto>>() {});
    }

    public UserDto getUserByName(String username) {
        return accountsServiceClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/account/user")
                        .queryParam("username", username)
                        .build())
                .retrieve()
                .body(UserDto.class);
    }

    public UserDto saveUser(UserDto userDto) {
        return accountsServiceClient.post()
                .uri("/account/user")
                .body(userDto)
                .retrieve()
                .body(UserDto.class);
    }

    public AccountDto getAccountByUserAndCurrency(Long userId, CurrencyEnum currency) {
        return accountsServiceClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/account/value/{userId}/{currency}")
                        .build(userId, currency))
                .retrieve()
                .body(AccountDto.class);
    }

    public AccountDto saveAccount(AccountDto accountDto) {
        return accountsServiceClient.post()
                .uri("/account")
                .body(accountDto)
                .retrieve()
                .body(AccountDto.class);
    }

    public void deleteAccount(AccountDto accountDto) {
        accountsServiceClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/account/{id}")
                        .build(accountDto.getId()))
                .retrieve()
                .toBodilessEntity();
    }
}
