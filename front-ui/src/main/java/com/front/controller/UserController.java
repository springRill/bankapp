package com.front.controller;

import com.front.dto.*;
import com.front.service.AccountsApiService;
import com.front.service.CashApiService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    private final AccountsApiService accountsApiService;

    private final CashApiService cashApiService;

    public UserController(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, AccountsApiService accountsApiService, CashApiService cashApiService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.accountsApiService = accountsApiService;
        this.cashApiService = cashApiService;
    }

    @PostMapping("/{login}/editPassword")
    public String editPassword(@PathVariable(name = "login") String login,
                               @RequestParam(name = "password") String password,
                               @RequestParam(name = "confirm_password") String confirm_password,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {

        if (!password.equals(confirm_password)) {
            redirectAttributes.addFlashAttribute("passwordErrors", List.of("Пароли не совпадают"));
            return "redirect:/main";
        }

        UserDto userDto = accountsApiService.getUserByName(login);
        userDto.setPassword(passwordEncoder.encode(password));
        userDto = accountsApiService.saveUser(userDto);

        authenticateUser(userDto.getUsername(), request);

        return "redirect:/main";
    }

    @PostMapping("/{login}/editUserAccounts")
    public String editUserAccounts(@PathVariable(name = "login") String login,
                                   @RequestParam(name = "name") String name,
                                   @RequestParam(name = "birthdate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthdate,
                                   @RequestParam(name = "account", required = false) List<String> selectedCurrencies,
                                   RedirectAttributes redirectAttributes) {

        List<String> userAccountsErrors = new ArrayList<>();

        UserDto userDto = accountsApiService.getUserByName(login);

        if(Period.between(birthdate, LocalDate.now()).getYears() < 18){
            userAccountsErrors.add("Вам должно быть больше 18 лет");
        }else {
            userDto.setPersonName(name);
            userDto.setDateOfBirth(birthdate);
            accountsApiService.saveUser(userDto);
        }

        Arrays.stream(CurrencyEnum.values())
                .forEach(currency -> {
                    AccountDto accountDto = accountsApiService.getAccountByUserAndCurrency(userDto.getId(), currency);
                    if (accountDto.getExists()) {
                        if (Objects.isNull(selectedCurrencies) || !selectedCurrencies.contains(accountDto.getCurrency().name())) {
                            if(accountDto.getValue()==0) {
                                accountsApiService.deleteAccount(accountDto);
                            }else {
                                userAccountsErrors.add("Баланс на счету %s не равен 0".formatted(currency.getTitle()));
                            }
                        }
                    } else {
                        if (!Objects.isNull(selectedCurrencies) && selectedCurrencies.contains(accountDto.getCurrency().name())) {
                            accountDto.setExists(true);
                            accountsApiService.saveAccount(accountDto);
                        }
                    }
                });

        redirectAttributes.addFlashAttribute("passwordErrors", userAccountsErrors);
        return "redirect:/main";
    }

    @PostMapping("/{login}/сash")
    public String сash(@PathVariable(name = "login") String login,
                       @RequestParam(name = "currency") CurrencyEnum currency,
                       @RequestParam(name = "value") Double value,
                       @RequestParam(name = "action") CashActionEnum action,
                       RedirectAttributes redirectAttributes) {

        UserDto userDto = accountsApiService.getUserByName(login);
        CashDto cashDto = new CashDto(userDto.getId(), currency, value, action);

        try {
            cashApiService.cash(cashDto);
        } catch (RestClientResponseException restClientResponseException) {
            redirectAttributes.addFlashAttribute("cashErrors", List.of(restClientResponseException.getResponseBodyAsString()));
        }
        return "redirect:/main";
    }

    @PostMapping("/user/{login}/transfer")
    public String transfer() {
        return "main";
    }

    private void authenticateUser(String username, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }

}
