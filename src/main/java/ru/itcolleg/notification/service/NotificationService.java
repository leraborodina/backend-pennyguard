package ru.itcolleg.notification.service;

import ru.itcolleg.notification.model.Notification;
import ru.itcolleg.user.exception.UserNotFoundException;

import java.util.List;

public interface NotificationService {
    List<Notification> getNotificationsByUserId(Long userId);

    Notification saveNotification(Long userId, String message) ;

    void deleteNotifications(Long userId);
}