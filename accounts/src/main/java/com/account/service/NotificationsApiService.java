package com.account.service;

import com.account.dto.NotificationDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import javax.management.OperationsException;

@Service
public class NotificationsApiService {

    RestClient notificationsServiceClient;

    public NotificationsApiService() {
        notificationsServiceClient = RestClient.create("http://localhost:8086");
    }

    public void notificate(NotificationDto notificationDto) {
        notificationsServiceClient.post()
                .uri("/notifications")
                .body(notificationDto)
                .retrieve()
                .toBodilessEntity();
    }
}
