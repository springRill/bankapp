package com.account.service;

import com.account.domain.Account;
import com.account.dto.AccountDto;
import com.account.mapper.AccountMapper;
import com.account.repository.AccountRepository;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountDto getAccountByUsername(String username) throws AccountNotFoundException {
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new AccountNotFoundException("User not found: " + username));
        return AccountMapper.toAccountDto(account);
    }

    public AccountDto createAccount(AccountDto accountDto) {
        Account account = accountRepository.save(AccountMapper.toAccount(accountDto));
        return AccountMapper.toAccountDto(account);
    }

}
