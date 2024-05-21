package ru.itcolleg.notification.service;

import ru.itcolleg.notification.model.Notification;
import ru.itcolleg.user.exception.UserNotFoundException;

import java.util.List;

/**
 * Сервис для работы с уведомлениями.
 * Service for working with notifications.
 */
public interface NotificationService {

    /**
     * Получить список уведомлений по идентификатору пользователя.
     * Get a list of notifications by user ID.
     *
     * @param userId Идентификатор пользователя
     * @return Список уведомлений
     */
    List<Notification> getNotificationsByUserId(Long userId);

    /**
     * Сохранить уведомление для указанного пользователя с заданным сообщением.
     * Save a notification for the specified user with the given message.
     *
     * @param userId  Идентификатор пользователя
     * @param message Сообщение уведомления
     * @return Сохраненное уведомление
     * @throws UserNotFoundException Если пользователь не найден
     */
    Notification saveNotification(Long userId, String message) throws UserNotFoundException;

    /**
     * Удалить все уведомления для указанного пользователя.
     * Delete all notifications for the specified user.
     *
     * @param userId Идентификатор пользователя
     */
    void deleteNotifications(Long userId);
}
