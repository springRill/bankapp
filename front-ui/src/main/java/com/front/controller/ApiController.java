package com.front.controller;

import com.front.dto.RateDto;
import com.front.service.TransferApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final TransferApiService transferApiService;

    public ApiController(TransferApiService transferApiService) {
        this.transferApiService = transferApiService;
    }


    @GetMapping("/rates")
    public List<RateDto> getRates() {
        return transferApiService.getExchangeDtoList().stream().map(exchangeDto -> {
            return new RateDto(exchangeDto.getCurrency().getTitle(), exchangeDto.getCurrency().name(), exchangeDto.getValue());
        }).toList();
    }

}
