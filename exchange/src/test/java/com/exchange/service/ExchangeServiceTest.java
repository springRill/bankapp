package com.exchange.service;
import com.exchange.domain.Exchange;
import com.exchange.dto.CurrencyEnum;
import com.exchange.dto.ExchangeDto;
import com.exchange.repository.ExchangeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(ExchangeService.class)
public class ExchangeServiceTest {

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Test
    void setExchange() {
        ExchangeDto exchangeDto1 = new ExchangeDto();
        exchangeDto1.setCurrency(CurrencyEnum.USD);
        exchangeDto1.setValue(1.1);
        exchangeService.setExchange(exchangeDto1);

        Optional<Exchange> found1 = Optional.ofNullable(exchangeRepository.findByCurrency(CurrencyEnum.USD));
        assertTrue(found1.isPresent());
        assertEquals(1.1, found1.get().getValue());


        ExchangeDto exchangeDto2 = new ExchangeDto();
        exchangeDto2.setCurrency(CurrencyEnum.RUB);
        exchangeDto2.setValue(1.2);
        exchangeService.setExchange(exchangeDto2);

        Optional<Exchange> found2 = Optional.ofNullable(exchangeRepository.findByCurrency(CurrencyEnum.RUB));
        assertTrue(found2.isPresent());
        assertEquals(1.2, found2.get().getValue());
    }

    @Test
    @DisplayName("Should return the correct exchange rate for a given currency from DB")
    void testGetExchange_shouldReturnCorrectValue() {
        Exchange mockExchange = new Exchange();
        mockExchange.setCurrency(CurrencyEnum.USD);
        mockExchange.setValue(1.05);
        exchangeRepository.save(mockExchange); // Сохраняем тестовые данные в БД

        Double result = exchangeService.getExchange(CurrencyEnum.USD);
        assertNotNull(result);
        assertEquals(1.05, result);
    }
}