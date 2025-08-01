package com.account.controller;

import com.account.dto.AccountDto;
import com.account.dto.CurrencyEnum;
import com.account.dto.UserDto;
import com.account.service.AccountService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFound(AccountNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleUserAlreadyExist(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}
