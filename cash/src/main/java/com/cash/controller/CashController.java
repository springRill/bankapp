package com.cash.controller;

import com.cash.dto.CashDto;
import com.cash.dto.NotificationDto;
import com.cash.dto.UserDto;
import com.cash.service.AccountsApiService;
import com.cash.service.NotificationsApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.OperationsException;

@RestController
@RequestMapping("/cash")
public class CashController {

    private final AccountsApiService accountsApiService;

    private final NotificationsApiService notificationsApiService;

    public CashController(AccountsApiService accountsApiService, NotificationsApiService notificationsApiService) {
        this.accountsApiService = accountsApiService;
        this.notificationsApiService = notificationsApiService;
    }

    @PostMapping("")
    public void cash(@RequestBody CashDto cashDto) throws OperationsException {
        UserDto userDto = accountsApiService.getUserById(cashDto.getUserId());
        notificationsApiService.notificate(new NotificationDto(userDto.getUsername(), cashDto.toString()));
        accountsApiService.cash(cashDto);
    }

    @ExceptionHandler(OperationsException.class)
    public ResponseEntity<String> handleOperationsException(OperationsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}

