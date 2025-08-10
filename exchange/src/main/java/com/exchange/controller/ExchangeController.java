package com.exchange.controller;

import com.exchange.dto.CurrencyEnum;
import com.exchange.dto.ExchangeDto;
import com.exchange.service.ExchangeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_EXCHANGE')")
    public void setExchange(@RequestBody ExchangeDto exchangeDto) {
        exchangeService.setExchange(exchangeDto);
    }

    @GetMapping("/{currency}")
    @PreAuthorize("hasRole('ROLE_EXCHANGE')")
    public Double getExchange(@PathVariable CurrencyEnum currency) {
        return exchangeService.getExchange(currency);
    }

}
