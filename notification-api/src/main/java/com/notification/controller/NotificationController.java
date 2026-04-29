package com.notification.controller;

import com.notification.dto.ApiResponse;
import com.notification.dto.NotificationHistoryDTO;
import com.notification.dto.SendNotificationDTO;
import com.notification.enums.NotificationChannel;
import com.notification.enums.NotificationStatus;
import com.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for Notification Delivery and History
 */
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Send a notification to a user
     * POST /api/notifications/send
     */
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<NotificationHistoryDTO>> sendNotification(
            @RequestBody SendNotificationDTO sendDTO) {
        NotificationHistoryDTO notification = notificationService.sendNotification(sendDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse<>(true, "Notification processed", notification));
    }

    /**
     * Get all notifications for a user
     * GET /api/notifications/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationHistoryDTO>>> getUserNotifications(
            @PathVariable Long userId) {
        List<NotificationHistoryDTO> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Notifications retrieved successfully", notifications)
        );
    }

    /**
     * Get notifications for a user filtered by status
     * GET /api/notifications/user/{userId}/status/{status}
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<ApiResponse<List<NotificationHistoryDTO>>> getUserNotificationsByStatus(
            @PathVariable Long userId,
            @PathVariable String status) {
        NotificationStatus notificationStatus = NotificationStatus.valueOf(status.toUpperCase());
        List<NotificationHistoryDTO> notifications = notificationService
            .getUserNotificationsByStatus(userId, notificationStatus);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Notifications retrieved successfully", notifications)
        );
    }

    /**
     * Get notifications for a user filtered by channel
     * GET /api/notifications/user/{userId}/channel/{channel}
     */
    @GetMapping("/user/{userId}/channel/{channel}")
    public ResponseEntity<ApiResponse<List<NotificationHistoryDTO>>> getUserNotificationsByChannel(
            @PathVariable Long userId,
            @PathVariable String channel) {
        NotificationChannel notificationChannel = NotificationChannel.valueOf(channel.toUpperCase());
        List<NotificationHistoryDTO> notifications = notificationService
            .getUserNotificationsByChannel(userId, notificationChannel);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Notifications retrieved successfully", notifications)
        );
    }

    /**
     * Get all notifications by status
     * GET /api/notifications/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<NotificationHistoryDTO>>> getNotificationsByStatus(
            @PathVariable String status) {
        NotificationStatus notificationStatus = NotificationStatus.valueOf(status.toUpperCase());
        List<NotificationHistoryDTO> notifications = notificationService.getNotificationsByStatus(notificationStatus);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Notifications retrieved successfully", notifications)
        );
    }

    /**
     * Get all notifications by channel
     * GET /api/notifications/channel/{channel}
     */
    @GetMapping("/channel/{channel}")
    public ResponseEntity<ApiResponse<List<NotificationHistoryDTO>>> getNotificationsByChannel(
            @PathVariable String channel) {
        NotificationChannel notificationChannel = NotificationChannel.valueOf(channel.toUpperCase());
        List<NotificationHistoryDTO> notifications = notificationService.getNotificationsByChannel(notificationChannel);
        return ResponseEntity.ok(
            new ApiResponse<>(true, "Notifications retrieved successfully", notifications)
        );
    }
}