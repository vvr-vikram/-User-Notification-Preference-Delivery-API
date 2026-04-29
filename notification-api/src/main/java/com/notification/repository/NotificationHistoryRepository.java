package com.notification.repository;

import com.notification.entity.NotificationHistory;
import com.notification.enums.NotificationChannel;
import com.notification.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for NotificationHistory entity
 */
@Repository
public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {
    List<NotificationHistory> findByUserId(Long userId);
    List<NotificationHistory> findByUserIdAndStatus(Long userId, NotificationStatus status);
    List<NotificationHistory> findByUserIdAndChannel(Long userId, NotificationChannel channel);
    List<NotificationHistory> findByStatus(NotificationStatus status);
    List<NotificationHistory> findByChannel(NotificationChannel channel);
}