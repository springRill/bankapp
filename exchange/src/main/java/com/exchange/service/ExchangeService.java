package com.exchange.service;

import com.exchange.domain.Exchange;
import com.exchange.dto.CurrencyEnum;
import com.exchange.dto.ExchangeDto;
import com.exchange.metrics.CustomMetrics;
import com.exchange.repository.ExchangeRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ExchangeService {

    private final ExchangeRepository exchangeRepository;

    private final CustomMetrics customMetrics;

    public ExchangeService(ExchangeRepository exchangeRepository, CustomMetrics customMetrics) {
        this.exchangeRepository = exchangeRepository;
        this.customMetrics = customMetrics;
    }

    public void setExchange(ExchangeDto exchangeDto){
        Exchange exchange = exchangeRepository.findByCurrency(exchangeDto.getCurrency());
        if(Objects.isNull(exchange)){
            exchange = new Exchange();
            exchange.setCurrency(exchangeDto.getCurrency());
        }
        exchange.setValue(exchangeDto.getValue());
        exchangeRepository.save(exchange);
        customMetrics.incrementCurrencyUpdate();
    }

    public Double getExchange(CurrencyEnum currency){
        Exchange exchange = exchangeRepository.findByCurrency(currency);
        return exchange.getValue();
    }

}
