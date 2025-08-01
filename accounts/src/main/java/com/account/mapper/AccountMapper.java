package com.account.mapper;

import com.account.domain.Account;
import com.account.dto.AccountDto;

public class AccountMapper {

    public static AccountDto toAccountDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setUsername(account.getUsername());
        accountDto.setPassword(account.getPassword());
        accountDto.setPersonName(account.getPersonName());
        accountDto.setDateOfBirth(account.getDateOfBirth());
        return accountDto;
    }

    public static Account toAccount(AccountDto accountDto) {
        Account account = new Account();
        account.setId(accountDto.getId());
        account.setUsername(accountDto.getUsername());
        account.setPassword(accountDto.getPassword());
        account.setPersonName(accountDto.getPersonName());
        account.setDateOfBirth(accountDto.getDateOfBirth());
        return account;
    }

}
