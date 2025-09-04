package com.cash.controller;

import com.cash.dto.CashDto;
import com.cash.dto.NotificationDto;
import com.cash.dto.UserDto;
import com.cash.service.AccountsApiService;
import com.cash.service.BlockerApiService;
import com.cash.service.NotificationsApiService;
import com.cash.service.NotificationsProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.OperationsException;

@RestController
@RequestMapping("/api/cash")
public class CashController {

    private final AccountsApiService accountsApiService;

    private final NotificationsApiService notificationsApiService;

    private final NotificationsProducer notificationsProducer;

    private final BlockerApiService blockerApiService;

    public CashController(AccountsApiService accountsApiService, NotificationsApiService notificationsApiService, NotificationsProducer notificationsProducer, BlockerApiService blockerApiService) {
        this.accountsApiService = accountsApiService;
        this.notificationsApiService = notificationsApiService;
        this.notificationsProducer = notificationsProducer;
        this.blockerApiService = blockerApiService;
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CASH')")
    public void cash(@RequestBody CashDto cashDto) throws OperationsException {
        if(!blockerApiService.validate()){
            throw new OperationsException("Операция заблокирована блокировщиком");
        }
        UserDto userDto = accountsApiService.getUserById(cashDto.getUserId());
//        notificationsApiService.notificate(new NotificationDto(userDto.getUsername(), cashDto.toString()));
        notificationsProducer.notificate(new NotificationDto(userDto.getUsername(), cashDto.toString()));

        accountsApiService.cash(cashDto);
    }

    @ExceptionHandler(OperationsException.class)
    public ResponseEntity<String> handleOperationsException(OperationsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}

