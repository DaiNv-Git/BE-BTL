package com.example.jobexchange.dto;

import com.example.jobexchange.domain.UserNotification;
import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String title,
        String message,
        boolean read,
        LocalDateTime createdAt,
        Long jobId
) {
    public static NotificationResponse from(UserNotification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.isReadFlag(),
                notification.getCreatedAt(),
                notification.getJobId()
        );
    }
}
