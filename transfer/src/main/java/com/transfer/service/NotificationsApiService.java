package com.transfer.service;

import com.transfer.dto.NotificationDto;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class NotificationsApiService {

    private final RestClient notificationsServiceClient;

    public NotificationsApiService(DiscoveryClient discoveryClient) {
        List<ServiceInstance> instances = discoveryClient.getInstances("notifications-api");
        if (instances.isEmpty()) {
            //notificationsServiceClient = RestClient.create("http://localhost:8086");
            throw new IllegalStateException("Service 'notifications-api' not found in DiscoveryClient");
        } else {
            ServiceInstance instance = instances.get(0);
            notificationsServiceClient = RestClient.create(instance.getUri().toString());
        }
    }

    public void notificate(NotificationDto notificationDto) {
        notificationsServiceClient.post()
                .uri("/notifications")
                .body(notificationDto)
                .retrieve()
                .toBodilessEntity();
    }
}
