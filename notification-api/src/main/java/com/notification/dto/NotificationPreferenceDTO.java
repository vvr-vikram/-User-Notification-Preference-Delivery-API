package com.notification.dto;

import com.notification.enums.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for NotificationPreference creation and update operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreferenceDTO {
    private Long id;
    private Long userId;
    private NotificationChannel channel;
    private Boolean enabled;
    private String createdAt;
    private String updatedAt;
}