package com.account.controller;

import com.account.dto.*;
import com.account.service.AccountService;
import com.account.service.ExchangeApiService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.OperationsException;
import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    private final ExchangeApiService exchangeApiService;

    public AccountController(AccountService accountService, ExchangeApiService exchangeApiService) {
        this.accountService = accountService;
        this.exchangeApiService = exchangeApiService;
    }

    @GetMapping("/users")
    public List<UserDto> getUsers() throws AccountNotFoundException {
        return accountService.getUsers();
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
            if(BigDecimal.valueOf(accountDto.getValue()).setScale(2, RoundingMode.HALF_EVEN).doubleValue() < BigDecimal.valueOf(cashDto.getValue()).setScale(2, RoundingMode.HALF_EVEN).doubleValue()) {
                throw new OperationsException("На счёте не достаточно средств");
            }
            accountDto.setValue(BigDecimal.valueOf(accountDto.getValue()).setScale(2, RoundingMode.HALF_EVEN).subtract(BigDecimal.valueOf(cashDto.getValue()).setScale(2, RoundingMode.HALF_EVEN)).doubleValue());
        }
        if (cashDto.getCashAction().equals(CashActionEnum.PUT)) {
            accountDto.setExists(true);
            accountDto.setValue(BigDecimal.valueOf(accountDto.getValue()).setScale(2, RoundingMode.HALF_EVEN).add(BigDecimal.valueOf(cashDto.getValue()).setScale(2, RoundingMode.HALF_EVEN)).doubleValue());
        }
        accountService.saveAccount(accountDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferDto transferDto) throws OperationsException {
        accountService.transfer(transferDto);
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
