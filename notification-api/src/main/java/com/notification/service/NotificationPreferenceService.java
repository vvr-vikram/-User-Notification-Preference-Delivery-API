package com.notification.service;

import com.notification.dto.NotificationPreferenceDTO;
import com.notification.entity.NotificationPreference;
import com.notification.entity.User;
import com.notification.enums.NotificationChannel;
import com.notification.exception.ResourceNotFoundException;
import com.notification.mapper.EntityMapper;
import com.notification.repository.NotificationPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for NotificationPreference management
 */
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationPreferenceService {

    private final NotificationPreferenceRepository preferenceRepository;
    private final UserService userService;
    private final EntityMapper entityMapper;

    /**
     * Initialize default preferences for a user (all channels disabled)
     */
    public void initializeDefaultPreferences(Long userId) {
        User user = userService.getUserEntityById(userId);

        for (NotificationChannel channel : NotificationChannel.values()) {
            // Check if preference already exists
            if (preferenceRepository.findByUserIdAndChannel(userId, channel).isEmpty()) {
                NotificationPreference preference = new NotificationPreference();
                preference.setUser(user);
                preference.setChannel(channel);
                preference.setEnabled(false); // Default: disabled
                preferenceRepository.save(preference);
            }
        }
    }

    /**
     * Enable a notification channel for a user
     */
    public NotificationPreferenceDTO enableChannel(Long userId, NotificationChannel channel) {
        User user = userService.getUserEntityById(userId);

        NotificationPreference preference = preferenceRepository
            .findByUserIdAndChannel(userId, channel)
            .orElseGet(() -> {
                NotificationPreference newPref = new NotificationPreference();
                newPref.setUser(user);
                newPref.setChannel(channel);
                return newPref;
            });

        preference.setEnabled(true);
        NotificationPreference saved = preferenceRepository.save(preference);
        return entityMapper.toPreferenceDTO(saved);
    }

    /**
     * Disable a notification channel for a user
     */
    public NotificationPreferenceDTO disableChannel(Long userId, NotificationChannel channel) {
        User user = userService.getUserEntityById(userId);

        NotificationPreference preference = preferenceRepository
            .findByUserIdAndChannel(userId, channel)
            .orElseGet(() -> {
                NotificationPreference newPref = new NotificationPreference();
                newPref.setUser(user);
                newPref.setChannel(channel);
                return newPref;
            });

        preference.setEnabled(false);
        NotificationPreference saved = preferenceRepository.save(preference);
        return entityMapper.toPreferenceDTO(saved);
    }

    /**
     * Update preference for a user
     */
    public NotificationPreferenceDTO updatePreference(Long userId, NotificationChannel channel, Boolean enabled) {
        if (enabled == null) {
            throw new IllegalArgumentException("Enabled status cannot be null");
        }

        return enabled ? enableChannel(userId, channel) : disableChannel(userId, channel);
    }

    /**
     * Get preferences for a user
     */
    @Transactional(readOnly = true)
    public List<NotificationPreferenceDTO> getUserPreferences(Long userId) {
        // Verify user exists
        userService.getUserEntityById(userId);

        return preferenceRepository.findByUserId(userId)
            .stream()
            .map(entityMapper::toPreferenceDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get preference for a specific channel
     */
    @Transactional(readOnly = true)
    public NotificationPreferenceDTO getChannelPreference(Long userId, NotificationChannel channel) {
        // Verify user exists
        userService.getUserEntityById(userId);

        NotificationPreference preference = preferenceRepository
            .findByUserIdAndChannel(userId, channel)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Preference not found for user: " + userId + ", channel: " + channel
            ));

        return entityMapper.toPreferenceDTO(preference);
    }

    /**
     * Check if a channel is enabled for a user
     */
    @Transactional(readOnly = true)
    public boolean isChannelEnabled(Long userId, NotificationChannel channel) {
        return preferenceRepository
            .findByUserIdAndChannel(userId, channel)
            .map(NotificationPreference::getEnabled)
            .orElse(false);
    }
}