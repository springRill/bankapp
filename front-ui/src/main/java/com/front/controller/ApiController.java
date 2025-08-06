package com.front.controller;

import com.front.dto.RateDto;
import com.front.service.ExchangeApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ExchangeApiService exchangeApiService;

    public ApiController(ExchangeApiService exchangeApiService) {
        this.exchangeApiService = exchangeApiService;
    }


    @GetMapping("/rates")
    public List<RateDto> getRates() {
        return exchangeApiService.getRateList();
    }

}
