package com.front.controller;

import com.front.dto.AccountDto;
import com.front.dto.CurrencyEnum;
import com.front.dto.UserDto;
import com.front.service.AccountsApiService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/main")
public class MainController {

    AccountsApiService accountsApiService;

    public MainController(AccountsApiService accountsApiService) {
        this.accountsApiService = accountsApiService;
    }

    @GetMapping("")
    public String getMain(Model model, Principal principal) {
        if (Objects.isNull(principal)) {
            return "main";
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto userDto = accountsApiService.getUserByName(username);

        model.addAttribute("login", userDto.getUsername());
        model.addAttribute("name", userDto.getPersonName());
        model.addAttribute("birthdate", userDto.getDateOfBirth());

        List<AccountDto> accountDtoList = Arrays.stream(CurrencyEnum.values())
                .map(currency -> {
                    return accountsApiService.getAccountByUserAndCurrency(userDto.getId(), currency);
                }).collect(Collectors.toList());

        model.addAttribute("accounts", accountDtoList);
//        model.addAttribute("", accountDto);
//        model.addAttribute("", accountDto);
//        model.addAttribute("", accountDto);
        return "main";
    }
}
