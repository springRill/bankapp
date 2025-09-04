package com.generator.service;

import com.generator.configuration.OAuth2TokenProvider;
import com.generator.dto.ExchangeDto;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Service
public class ExchangeProducer {

    private final OAuth2TokenProvider tokenProvider;

    private final KafkaTemplate<String, ExchangeDto> kafkaTemplate;


    public ExchangeProducer(OAuth2TokenProvider tokenProvider, KafkaTemplate<String, ExchangeDto> kafkaTemplate) {
        this.tokenProvider = tokenProvider;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void setExchange(ExchangeDto exchangeDto) {
        String token = tokenProvider.getAccessToken();

        System.out.println("token=" + token);

        ProducerRecord<String, ExchangeDto> record = new ProducerRecord<>("exchange", exchangeDto.getCurrency().name(), exchangeDto);
        record.headers().add("Authorization", ("Bearer " + token).getBytes(StandardCharsets.UTF_8));

        kafkaTemplate.send(record);
/*



        CompletableFuture<SendResult<String, ExchangeDto>> future = kafkaTemplate.send(record);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + exchangeDto + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" + exchangeDto + "] due to : " + ex.getMessage());
            }
        });
*/

    }

}
