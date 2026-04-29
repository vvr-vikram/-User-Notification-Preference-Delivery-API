package com.notification.enums;


public enum NotificationChannel {
    EMAIL("Email"),
    SMS("Short Message Service"),
    PUSH("Push Notification");

    private final String displayName;

    NotificationChannel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}