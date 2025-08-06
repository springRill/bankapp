package com.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {

    private Long userId;
    private CurrencyEnum fromCurrency;
    private CurrencyEnum toCurrency;
    private Double value;
    private Long toUserId;

}
