package com.transfer.service;

import com.transfer.dto.NotificationDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NotificationsApiService {

    private final RestClient notificationsServiceClient;

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
