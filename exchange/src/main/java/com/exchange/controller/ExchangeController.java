package com.exchange.controller;

import com.exchange.dto.CurrencyEnum;
import com.exchange.dto.ExchangeDto;
import com.exchange.service.ExchangeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @PostMapping("")
    public void setExchange(@RequestBody ExchangeDto exchangeDto) {
        exchangeService.setExchange(exchangeDto);
    }

    @GetMapping("/{currency}")
    public Double getExchange(@PathVariable CurrencyEnum currency) {
        return exchangeService.getExchange(currency);
    }

}
