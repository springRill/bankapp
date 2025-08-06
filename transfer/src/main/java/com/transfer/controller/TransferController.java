package com.transfer.controller;

import com.transfer.dto.TransferDto;
import com.transfer.service.AccountsApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.OperationsException;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private final AccountsApiService accountsApiService;

    public TransferController(AccountsApiService accountsApiService) {
        this.accountsApiService = accountsApiService;
    }

    @PostMapping("")
    public void transfer(@RequestBody TransferDto transferDto) throws OperationsException {
        accountsApiService.transfer(transferDto);
    }

    @ExceptionHandler(OperationsException.class)
    public ResponseEntity<String> handleOperationsException(OperationsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
