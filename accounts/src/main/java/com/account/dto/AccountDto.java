package com.account.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AccountDto {

    private Long id;

    private String username;
    private String password;

    private String personName;
    private LocalDate dateOfBirth;
}
