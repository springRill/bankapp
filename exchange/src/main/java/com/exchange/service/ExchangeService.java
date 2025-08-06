package com.exchange.service;

import com.exchange.domain.Exchange;
import com.exchange.dto.CurrencyEnum;
import com.exchange.dto.ExchangeDto;
import com.exchange.repository.ExchangeRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ExchangeService {

    private final ExchangeRepository exchangeRepository;

    public ExchangeService(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    public void setExchange(ExchangeDto exchangeDto){
        Exchange exchange = exchangeRepository.findByCurrency(exchangeDto.getCurrency());
        if(Objects.isNull(exchange)){
            exchange = new Exchange();
            exchange.setCurrency(exchangeDto.getCurrency());
        }
        exchange.setValue(exchangeDto.getValue());
        exchangeRepository.save(exchange);
    }

    public Double getExchange(CurrencyEnum currency){
        Exchange exchange = exchangeRepository.findByCurrency(currency);
        return exchange.getValue();
    }

}
