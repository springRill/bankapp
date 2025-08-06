package com.account.service;

import com.account.dto.CurrencyEnum;
import org.springframework.stereotype.Service;

@Service
public class ExchangeApiService {

    public Double getRate(CurrencyEnum currency){
        switch (currency){
            case CNY -> {return 11D;}
            case USD -> {return 80D;}
            case RUB -> {return 1D;}
            default -> {return null;}
        }
    }
}
