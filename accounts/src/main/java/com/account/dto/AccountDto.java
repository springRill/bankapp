package com.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDto {

    private Long id;

    private Long userId;
    private CurrencyEnum currency;
    private Boolean exists;
    private Double value;

}
