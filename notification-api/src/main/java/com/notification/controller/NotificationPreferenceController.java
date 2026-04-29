package com.notification.controller;

import com.notification.dto.ApiResponse;
import com.notification.dto.NotificationPreferenceDTO;
import com.notification.enums.NotificationChannel;
import com.notification.service.NotificationPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Notification Preference Management
 */
@RestController
@RequestMapping("/users/{userId}/preferences")
@RequiredArgsConstructor
public class NotificationPreferenceController {

    private final NotificationPreferenceService preferenceService;

    /**
     * Get all preferences for a user
     * GET /api/users/{userId}/preferences
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationPreferenceDTO>>> getUserPreferences(
            @PathVariable Long userId) {
        List<NotificationPreferenceDTO> preferences = preferenceService.getUserPreferences(userId);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Preferences retrieved successfully", preferences)
        );
    }

    /**
     * Get preference for a specific channel
     * GET /api/users/{userId}/preferences/{channel}
     */
    @GetMapping("/{channel}")
    public ResponseEntity<ApiResponse<NotificationPreferenceDTO>> getChannelPreference(
            @PathVariable Long userId,
            @PathVariable String channel) {
        NotificationChannel notificationChannel = NotificationChannel.valueOf(channel.toUpperCase());
        NotificationPreferenceDTO preference = preferenceService.getChannelPreference(userId, notificationChannel);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Preference retrieved successfully", preference)
        );
    }

    /**
     * Enable a notification channel
     * POST /api/users/{userId}/preferences/{channel}/enable
     */
    @PostMapping("/{channel}/enable")
    public ResponseEntity<ApiResponse<NotificationPreferenceDTO>> enableChannel(
            @PathVariable Long userId,
            @PathVariable String channel) {
        NotificationChannel notificationChannel = NotificationChannel.valueOf(channel.toUpperCase());
        NotificationPreferenceDTO preference = preferenceService.enableChannel(userId, notificationChannel);
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ApiResponse<>(true, "Channel enabled successfully", preference));
    }

    /**
     * Disable a notification channel
     * POST /api/users/{userId}/preferences/{channel}/disable
     */
    @PostMapping("/{channel}/disable")
    public ResponseEntity<ApiResponse<NotificationPreferenceDTO>> disableChannel(
            @PathVariable Long userId,
            @PathVariable String channel) {
        NotificationChannel notificationChannel = NotificationChannel.valueOf(channel.toUpperCase());
        NotificationPreferenceDTO preference = preferenceService.disableChannel(userId, notificationChannel);
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ApiResponse<>(true, "Channel disabled successfully", preference));
    }

    /**
     * Update preference for a channel
     * PUT /api/users/{userId}/preferences/{channel}
     */
    @PutMapping("/{channel}")
    public ResponseEntity<ApiResponse<NotificationPreferenceDTO>> updatePreference(
            @PathVariable Long userId,
            @PathVariable String channel,
            @RequestBody Map<String, Boolean> request) {
        NotificationChannel notificationChannel = NotificationChannel.valueOf(channel.toUpperCase());
        Boolean enabled = request.get("enabled");
        
        if (enabled == null) {
            throw new IllegalArgumentException("'enabled' field is required");
        }

        NotificationPreferenceDTO preference = preferenceService.updatePreference(userId, notificationChannel, enabled);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Preference updated successfully", preference)
        );
    }
}