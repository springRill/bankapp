package com.account.service;

import com.account.domain.Account;
import com.account.domain.User;
import com.account.dto.AccountDto;
import com.account.dto.CurrencyEnum;
import com.account.dto.UserDto;
import com.account.mapper.AccountMapper;
import com.account.mapper.UserMapper;
import com.account.repository.AccountRepository;
import com.account.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;

@Service
public class AccountService {

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    public AccountService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public UserDto getUserByUsername(String username) throws AccountNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AccountNotFoundException("User not found: " + username));
        return UserMapper.toUserDto(user);
    }

    public UserDto saveUser(UserDto userDto) {
        User user = userRepository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    public AccountDto getAccountsByUserIdAndCurrency(Long userId, CurrencyEnum currency) {
        return accountRepository.findByUserIdAndCurrency(userId, currency)
                .map(AccountMapper::toAccountDto)
                .orElse(new AccountDto(null, userId, currency, false, 0.0));
    }

    public AccountDto saveAccount(AccountDto accountDto) {
        Account account = accountRepository.save(AccountMapper.toAccount(accountDto));
        return AccountMapper.toAccountDto(account);
    }

    public void deleteAccountById(Long accountId) {
        accountRepository.deleteById(accountId);
    }

}
