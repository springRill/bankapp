package com.account.mapper;

import com.account.domain.Account;
import com.account.domain.User;
import com.account.dto.AccountDto;

public class AccountMapper {

    public static AccountDto toAccountDto(Account account){
        return new AccountDto(account.getId(), account.getUser().getId(), account.getCurrency(), true, account.getValue());
    }

    public static Account toAccount(AccountDto accountDto){
        return new Account(accountDto.getId(), new User(accountDto.getUserId()), accountDto.getCurrency(), accountDto.getValue());
    }

}
