package com.notification.enums;

public enum NotificationStatus {
    SENT("Notification successfully sent"),
    FAILED("Notification delivery failed"),
    PENDING("Notification pending delivery");

    private final String description;

    NotificationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}