package com.notification.dto;

import com.notification.enums.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for sending notifications
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationDTO {
    private Long userId;
    private NotificationChannel channel;
    private String message;
}