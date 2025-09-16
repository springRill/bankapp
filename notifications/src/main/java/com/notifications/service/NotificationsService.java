package com.notifications.service;

import com.notifications.dto.NotificationDto;
import com.notifications.metrics.CustomMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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
        log.info("Уведомление для пользователя {}: {}", notificationDto.getLogin(), notificationDto.getMessage());
//        System.out.printf("Уведомление для пользователя %s: %s%n", notificationDto.getLogin(), notificationDto.getMessage());
    }
}
