package com.account.service;

import com.account.domain.Account;
import com.account.domain.User;
import com.account.dto.AccountDto;
import com.account.dto.CurrencyEnum;
import com.account.dto.TransferDto;
import com.account.dto.UserDto;
import com.account.mapper.AccountMapper;
import com.account.mapper.UserMapper;
import com.account.repository.AccountRepository;
import com.account.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.management.OperationsException;
import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    public AccountService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public List<UserDto> getUsers() {
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = userList.stream().map(user -> {
            UserDto userDto = new UserDto();
            userDto.setUsername(user.getUsername());
            userDto.setPersonName(user.getPersonName());
            return userDto;
        }).collect(Collectors.toList());
        return userDtoList;
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

    @Transactional
    public void transfer(TransferDto transferDto) throws OperationsException {
        AccountDto fromAccountDto = getAccountsByUserIdAndCurrency(transferDto.getUserId(), transferDto.getFromExchange().getCurrency());
        if(BigDecimal.valueOf(fromAccountDto.getValue()).setScale(2, RoundingMode.HALF_EVEN).doubleValue() < BigDecimal.valueOf(transferDto.getValue()).setScale(2, RoundingMode.HALF_EVEN).doubleValue()) {
            throw new OperationsException("На счёте не достаточно средств");
        }
        Double fromValue = fromAccountDto.getValue() - transferDto.getValue();
        fromAccountDto.setValue(BigDecimal.valueOf(fromValue).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        accountRepository.save(AccountMapper.toAccount(fromAccountDto));

        AccountDto toAccountDto = getAccountsByUserIdAndCurrency(transferDto.getToUserId(), transferDto.getToExchange().getCurrency());
        if(Objects.isNull(toAccountDto.getId())){
            throw new OperationsException("У пользователя %s нет счёта в нужной валюте");
        }

        Double rubValue = transferDto.getValue() * transferDto.getFromExchange().getValue();
        Double currencyValue = rubValue / transferDto.getToExchange().getValue();

        Double toValue = toAccountDto.getValue() + currencyValue;
        toAccountDto.setValue(BigDecimal.valueOf(toValue).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        accountRepository.save(AccountMapper.toAccount(toAccountDto));
    }

}
