package com.front.service;

import com.front.dto.AccountDto;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankUserDetailsService implements UserDetailsService {

    AccountsApiService accountsApiService;

    public BankUserDetailsService(AccountsApiService accountsApiService) {
        this.accountsApiService = accountsApiService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountDto accountDto = accountsApiService.getAccountByName(username);
        return new User(accountDto.getUsername(), accountDto.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

}
