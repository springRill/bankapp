package com.exchange.controller;

import com.exchange.dto.ExchangeDto;
import com.exchange.service.ExchangeService;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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

    private final Tracer tracer;

    public ExchangeConsumer(JwtDecoder jwtDecoder, ExchangeService exchangeService, Tracer tracer) {
        this.jwtDecoder = jwtDecoder;
        this.exchangeService = exchangeService;
        this.tracer = tracer;
    }

    @KafkaListener(topics = "exchange", groupId = "exchange-group")
    public void consume(ConsumerRecord<String, ExchangeDto> record,
                        @Header("Authorization") String authorizationHeader) {

        // Создаём новый спан и устанавливаем parent, если TraceId пришёл в заголовках
        Span span = tracer.nextSpan().name("kafka-consumer");
        String traceIdHeader = record.headers().lastHeader("b3") != null ?
                new String(record.headers().lastHeader("b3").value()) : null;

        // Если пришёл traceId, можно назначить его родителем
        if (traceIdHeader != null) {
            // Micrometer/Brave умеет автоматически подтягивать parent через пропагаторы
        }

        try (Tracer.SpanInScope ws = tracer.withSpan(span.start())) {

            // Теги спана
            span.tag("kafka.topic", record.topic());
            span.tag("kafka.key", record.key());

            // JWT проверка
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                span.tag("kafka.jwt", "missing");
                return;
            }

            String token = authorizationHeader.substring(7);
            Jwt jwt = jwtDecoder.decode(token);

            List<String> roles = ((Map<String, List<String>>) jwt.getClaim("realm_access")).get("roles");
            if (roles == null || !roles.contains("ROLE_EXCHANGE")) {
                span.tag("kafka.jwt", "no_role_exchange");
                return;
            }

            span.event("kafka.processed.success");

            exchangeService.setExchange(record.value());

        } catch (JwtException e) {
            span.tag("kafka.jwt", "invalid");
            span.tag("kafka.error", e.getMessage());
        } finally {
            span.end();
        }
    }
}
