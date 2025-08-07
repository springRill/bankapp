package com.cash.service;

import com.cash.dto.NotificationDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

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
