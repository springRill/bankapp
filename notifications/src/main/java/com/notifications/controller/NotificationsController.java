package com.notifications.controller;

import com.notifications.dto.NotificationDto;
import com.notifications.service.NotificationsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationsController {

    private final NotificationsService notificationsService;

    public NotificationsController(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    @PostMapping("")
    public void notificate(@RequestBody NotificationDto notificationDto) {
        notificationsService.sendNotification(notificationDto);
    }
}
