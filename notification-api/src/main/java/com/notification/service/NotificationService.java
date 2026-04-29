package com.notification.service;

import com.notification.dto.NotificationHistoryDTO;
import com.notification.dto.SendNotificationDTO;
import com.notification.entity.NotificationHistory;
import com.notification.entity.User;
import com.notification.enums.NotificationChannel;
import com.notification.enums.NotificationStatus;
import com.notification.exception.NotificationChannelDisabledException;
import com.notification.mapper.EntityMapper;
import com.notification.repository.NotificationHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Notification delivery and history tracking
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationService {

    private final NotificationHistoryRepository historyRepository;
    private final NotificationPreferenceService preferenceService;
    private final UserService userService;
    private final EntityMapper entityMapper;

    /**
     * Send a notification to a user
     * Validates user existence and channel enabled status before sending
     */
    public NotificationHistoryDTO sendNotification(SendNotificationDTO sendDTO) {
        // Verify user exists
        User user = userService.getUserEntityById(sendDTO.getUserId());

        // Validate channel is enabled
        if (!preferenceService.isChannelEnabled(sendDTO.getUserId(), sendDTO.getChannel())) {
            throw new NotificationChannelDisabledException(
                "Notification channel " + sendDTO.getChannel() + 
                " is disabled for user: " + sendDTO.getUserId()
            );
        }

        // Simulate sending notification
        NotificationHistory history = new NotificationHistory();
        history.setUser(user);
        history.setChannel(sendDTO.getChannel());
        history.setMessage(sendDTO.getMessage());
        history.setSentAt(LocalDateTime.now());

        try {
            // Simulate delivery (in real system, this would call actual delivery APIs)
            simulateNotificationDelivery(sendDTO, history);
            history.setStatus(NotificationStatus.SENT);
            log.info("Notification sent successfully to user {} via {}", 
                sendDTO.getUserId(), sendDTO.getChannel());
        } catch (Exception e) {
            history.setStatus(NotificationStatus.FAILED);
            history.setFailureReason(e.getMessage());
            log.error("Failed to send notification to user {} via {}: {}", 
                sendDTO.getUserId(), sendDTO.getChannel(), e.getMessage());
        }

        NotificationHistory savedHistory = historyRepository.save(history);
        return entityMapper.toHistoryDTO(savedHistory);
    }

    /**
     * Simulate notification delivery to different channels
     * In a real system, this would integrate with email/SMS/push services
     */
    private void simulateNotificationDelivery(SendNotificationDTO sendDTO, NotificationHistory history) {
        switch (sendDTO.getChannel()) {
            case EMAIL:
                simulateEmailDelivery(sendDTO, history);
                break;
            case SMS:
                simulateSmsDelivery(sendDTO, history);
                break;
            case PUSH:
                simulatePushDelivery(sendDTO, history);
                break;
            default:
                throw new IllegalArgumentException("Unknown channel: " + sendDTO.getChannel());
        }
    }

    /**
     * Simulate email delivery
     */
    private void simulateEmailDelivery(SendNotificationDTO sendDTO, NotificationHistory history) {
        log.debug("Simulating email delivery to user {}", sendDTO.getUserId());
        // In real system: call email service (SendGrid, AWS SES, etc.)
        // Simulate occasional failures (10% failure rate)
        if (Math.random() < 0.1) {
            throw new RuntimeException("Email service temporarily unavailable");
        }
    }

    /**
     * Simulate SMS delivery
     */
    private void simulateSmsDelivery(SendNotificationDTO sendDTO, NotificationHistory history) {
        log.debug("Simulating SMS delivery to user {}", sendDTO.getUserId());
        // In real system: call SMS service (Twilio, AWS SNS, etc.)
        // Simulate occasional failures (5% failure rate)
        if (Math.random() < 0.05) {
            throw new RuntimeException("SMS service temporarily unavailable");
        }
    }

    /**
     * Simulate push notification delivery
     */
    private void simulatePushDelivery(SendNotificationDTO sendDTO, NotificationHistory history) {
        log.debug("Simulating push notification delivery to user {}", sendDTO.getUserId());
        // In real system: call push service (Firebase Cloud Messaging, etc.)
        // Simulate occasional failures (15% failure rate)
        if (Math.random() < 0.15) {
            throw new RuntimeException("Push notification service temporarily unavailable");
        }
    }

    /**
     * Get all notifications for a user
     */
    @Transactional(readOnly = true)
    public List<NotificationHistoryDTO> getUserNotifications(Long userId) {
        userService.getUserEntityById(userId);
        
        return historyRepository.findByUserId(userId)
            .stream()
            .map(entityMapper::toHistoryDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get notifications for a user filtered by status
     */
    @Transactional(readOnly = true)
    public List<NotificationHistoryDTO> getUserNotificationsByStatus(Long userId, NotificationStatus status) {
        userService.getUserEntityById(userId);
        
        return historyRepository.findByUserIdAndStatus(userId, status)
            .stream()
            .map(entityMapper::toHistoryDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get notifications for a user filtered by channel
     */
    @Transactional(readOnly = true)
    public List<NotificationHistoryDTO> getUserNotificationsByChannel(Long userId, NotificationChannel channel) {
        userService.getUserEntityById(userId);
        
        return historyRepository.findByUserIdAndChannel(userId, channel)
            .stream()
            .map(entityMapper::toHistoryDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get all notifications by status
     */
    @Transactional(readOnly = true)
    public List<NotificationHistoryDTO> getNotificationsByStatus(NotificationStatus status) {
        return historyRepository.findByStatus(status)
            .stream()
            .map(entityMapper::toHistoryDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get all notifications by channel
     */
    @Transactional(readOnly = true)
    public List<NotificationHistoryDTO> getNotificationsByChannel(NotificationChannel channel) {
        return historyRepository.findByChannel(channel)
            .stream()
            .map(entityMapper::toHistoryDTO)
            .collect(Collectors.toList());
    }
}