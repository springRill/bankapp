package com.front.service;

import com.front.dto.CurrencyEnum;
import com.front.dto.RateDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExchangeApiService {

    public Double getRate(CurrencyEnum currency){
        switch (currency){
            case RUB -> {return 1D;}
            case USD -> {return 80D;}
            case CNY -> {return 11D;}
            default -> {return null;}
        }
    }

    public List<RateDto> getRateList(){
        List<RateDto> rateDtoList = new ArrayList<>();
        rateDtoList.add(new RateDto(CurrencyEnum.RUB.getTitle(), CurrencyEnum.RUB.toString(), getRate(CurrencyEnum.RUB)));
        rateDtoList.add(new RateDto(CurrencyEnum.USD.getTitle(), CurrencyEnum.USD.toString(), getRate(CurrencyEnum.USD)));
        rateDtoList.add(new RateDto(CurrencyEnum.CNY.getTitle(), CurrencyEnum.CNY.toString(), getRate(CurrencyEnum.CNY)));
        return rateDtoList;
    }

}
