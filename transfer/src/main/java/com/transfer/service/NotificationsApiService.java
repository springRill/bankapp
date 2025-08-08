package com.transfer.service;

import com.transfer.dto.NotificationDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NotificationsApiService {

    private final RestClient notificationsServiceClient;

    public NotificationsApiService(RestClient.Builder builder) {
        //notificationsServiceClient = RestClient.create("http://localhost:8086/api");
        this.notificationsServiceClient = builder.baseUrl("http://notifications-api/api").build();
    }

    public void notificate(NotificationDto notificationDto) {
        notificationsServiceClient.post()
                .uri("/notifications")
                .body(notificationDto)
                .retrieve()
                .toBodilessEntity();
    }
}
