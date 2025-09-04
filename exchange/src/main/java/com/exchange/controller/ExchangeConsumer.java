package com.exchange.controller;

import com.exchange.dto.ExchangeDto;
import com.exchange.service.ExchangeService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ExchangeConsumer {

    private final JwtDecoder jwtDecoder;

    private final ExchangeService exchangeService;

    public ExchangeConsumer(JwtDecoder jwtDecoder, ExchangeService exchangeService) {
        this.jwtDecoder = jwtDecoder;
        this.exchangeService = exchangeService;
    }

    @KafkaListener(topics = "exchange", groupId = "exchange-group")
    public void consume(ExchangeDto exchangeDto, @Header("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.err.println("JWT token is missing");
            return;
        }

        String token = authorizationHeader.substring(7);

        try {
            Jwt jwt = jwtDecoder.decode(token);

            List<String> roles = ((Map<String, List<String>>) jwt.getClaim("realm_access")).get("roles");
            if (roles == null || !roles.contains("ROLE_EXCHANGE")) {
                System.err.println("User does not have ROLE_EXCHANGE");
                return;
            }

            System.out.println("Received exchange message: " + exchangeDto);
            exchangeService.setExchange(exchangeDto);

        } catch (JwtException e) {
            System.err.println("Invalid JWT: " + e.getMessage());
        }
    }
}
