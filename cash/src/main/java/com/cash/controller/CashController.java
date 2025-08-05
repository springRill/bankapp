package com.cash.controller;

import com.cash.dto.CashDto;
import com.cash.service.AccountsApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.OperationsException;

@RestController
@RequestMapping("/cash")
public class CashController {

    private final AccountsApiService accountsApiService;

    public CashController(AccountsApiService accountsApiService) {
        this.accountsApiService = accountsApiService;
    }

    @PostMapping("")
    public void cash(@RequestBody CashDto cashDto) throws OperationsException {
        accountsApiService.cash(cashDto);
    }

    @ExceptionHandler(OperationsException.class)
    public ResponseEntity<String> handleOperationsException(OperationsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}

