package com.notification.mapper;

import com.notification.dto.NotificationHistoryDTO;
import com.notification.dto.NotificationPreferenceDTO;
import com.notification.dto.UserDTO;
import com.notification.entity.NotificationHistory;
import com.notification.entity.NotificationPreference;
import com.notification.entity.User;
import org.springframework.stereotype.Component;
import java.time.format.DateTimeFormatter;

/**
 * Mapper utility for converting between entities and DTOs
 */
@Component
public class EntityMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Convert User entity to UserDTO
     */
    public UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getCreatedAt() != null ? user.getCreatedAt().format(DATE_FORMATTER) : null,
            user.getUpdatedAt() != null ? user.getUpdatedAt().format(DATE_FORMATTER) : null
        );
    }

    /**
     * Convert UserDTO to User entity
     */
    public User toUserEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        return user;
    }

    /**
     * Convert NotificationPreference entity to NotificationPreferenceDTO
     */
    public NotificationPreferenceDTO toPreferenceDTO(NotificationPreference preference) {
        if (preference == null) {
            return null;
        }
        return new NotificationPreferenceDTO(
            preference.getId(),
            preference.getUser().getId(),
            preference.getChannel(),
            preference.getEnabled(),
            preference.getCreatedAt() != null ? preference.getCreatedAt().format(DATE_FORMATTER) : null,
            preference.getUpdatedAt() != null ? preference.getUpdatedAt().format(DATE_FORMATTER) : null
        );
    }

    /**
     * Convert NotificationHistory entity to NotificationHistoryDTO
     */
    public NotificationHistoryDTO toHistoryDTO(NotificationHistory history) {
        if (history == null) {
            return null;
        }
        return new NotificationHistoryDTO(
            history.getId(),
            history.getUser().getId(),
            history.getChannel(),
            history.getMessage(),
            history.getStatus(),
            history.getSentAt() != null ? history.getSentAt().format(DATE_FORMATTER) : null,
            history.getFailureReason(),
            history.getCreatedAt() != null ? history.getCreatedAt().format(DATE_FORMATTER) : null
        );
    }
}