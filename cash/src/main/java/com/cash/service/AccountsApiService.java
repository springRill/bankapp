package com.cash.service;

import com.cash.dto.CashDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import javax.management.OperationsException;

@Service
public class AccountsApiService {

    RestClient accountsServiceClient;

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

}
