package com.notification.exception;

/**
 * Exception thrown when a disabled notification channel is used
 */
@SuppressWarnings("serial")
public class NotificationChannelDisabledException extends RuntimeException {
    public NotificationChannelDisabledException(String message) {
        super(message);
    }

    public NotificationChannelDisabledException(String message, Throwable cause) {
        super(message, cause);
    }
}