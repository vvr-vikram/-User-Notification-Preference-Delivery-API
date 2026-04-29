package com.notification.dto;

import com.notification.enums.NotificationChannel;
import com.notification.enums.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for NotificationHistory responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationHistoryDTO {
    private Long id;
    private Long userId;
    private NotificationChannel channel;
    private String message;
    private NotificationStatus status;
    private String sentAt;
    private String failureReason;
    private String createdAt;
}