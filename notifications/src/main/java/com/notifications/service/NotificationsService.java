package com.notifications.service;

import com.notifications.dto.NotificationDto;
import com.notifications.metrics.CustomMetrics;
import org.springframework.stereotype.Service;

@Service
public class NotificationsService {

    private Integer notificationCount = 0;

    private final CustomMetrics customMetrics;

    public NotificationsService(CustomMetrics customMetrics) {
        this.customMetrics = customMetrics;
    }

    public void sendNotification(NotificationDto notificationDto){
        notificationCount++;
        if(notificationCount % 2 == 0){
            customMetrics.incrementFailureNotifications(notificationDto.getLogin());
            return;
        }
        System.out.printf("Уведомление для пользователя %s: %s%n", notificationDto.getLogin(), notificationDto.getMessage());
    }
}
