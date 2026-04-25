package com.example.jobexchange.controller;

import com.example.jobexchange.config.AuthContext;
import com.example.jobexchange.dto.NotificationResponse;
import com.example.jobexchange.repository.UserNotificationRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final UserNotificationRepository notifications;
    private final AuthContext authContext;

    public NotificationController(UserNotificationRepository notifications, AuthContext authContext) {
        this.notifications = notifications;
        this.authContext = authContext;
    }

    @GetMapping("/me")
    public List<NotificationResponse> myNotifications(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        var user = authContext.requireUser(userId);
        return notifications.findByRecipientIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }
}
