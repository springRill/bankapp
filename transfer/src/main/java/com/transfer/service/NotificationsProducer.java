package com.transfer.service;

import com.transfer.configuration.OAuth2TokenProvider;
import com.transfer.dto.NotificationDto;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class NotificationsProducer {

    private final OAuth2TokenProvider tokenProvider;

    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;


    public NotificationsProducer(OAuth2TokenProvider tokenProvider, KafkaTemplate<String, NotificationDto> kafkaTemplate) {
        this.tokenProvider = tokenProvider;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void notificate(NotificationDto notificationDto) {
        String token = tokenProvider.getAccessToken();

        System.out.println("token=" + token);

        ProducerRecord<String, NotificationDto> record = new ProducerRecord<>("notification", notificationDto.getLogin(), notificationDto);
        record.headers().add("Authorization", ("Bearer " + token).getBytes(StandardCharsets.UTF_8));

        kafkaTemplate.send(record);
    }

}
