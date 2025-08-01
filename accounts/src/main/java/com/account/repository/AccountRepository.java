package com.account.repository;

import com.account.domain.Account;
import com.account.dto.CurrencyEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Optional<Account> findByUserIdAndCurrency(Long userId, CurrencyEnum currency);
}
