package com.exchange.repository;

import com.exchange.domain.Exchange;
import com.exchange.dto.CurrencyEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExchangeRepository extends CrudRepository<Exchange, Long> {

    Exchange findByCurrency(CurrencyEnum currency);

}
