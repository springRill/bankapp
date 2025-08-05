package com.account.controller;

import com.account.dto.*;
import com.account.service.AccountService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.OperationsException;
import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/user")
    public UserDto getUserByName(@RequestParam String username) throws AccountNotFoundException {
        return accountService.getUserByUsername(username);
    }

    @PostMapping("/user")
    public UserDto saveUser(@RequestBody UserDto userDto) {
        return accountService.saveUser(userDto);
    }

    @GetMapping("/value/{userId}/{currency}")
    public AccountDto getAccountByUserIdAndCurrency(@PathVariable Long userId,
                                                    @PathVariable CurrencyEnum currency) {
        return accountService.getAccountsByUserIdAndCurrency(userId, currency);
    }

    @PostMapping("")
    public AccountDto saveAccount(@RequestBody AccountDto accountDto) {
        return accountService.saveAccount(accountDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccountById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/cash")
    public ResponseEntity<Void> cash(@RequestBody CashDto cashDto) throws OperationsException {
        AccountDto accountDto = accountService.getAccountsByUserIdAndCurrency(cashDto.getUserId(), cashDto.getCurrency());
        if (cashDto.getCashAction().equals(CashActionEnum.GET)) {
            if(accountDto.getValue() < cashDto.getValue()) {
                throw new OperationsException("На счёте не достаточно средств");
            }
            accountDto.setValue(accountDto.getValue() - cashDto.getValue());
        }
        if (cashDto.getCashAction().equals(CashActionEnum.PUT)) {
            accountDto.setExists(true);
            accountDto.setValue(accountDto.getValue() + cashDto.getValue());
        }
        accountService.saveAccount(accountDto);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(OperationsException.class)
    public ResponseEntity<String> handleOperationsException(OperationsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFound(AccountNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleUserAlreadyExist(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}
