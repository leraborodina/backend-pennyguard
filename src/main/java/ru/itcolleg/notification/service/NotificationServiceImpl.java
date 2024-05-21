package ru.itcolleg.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itcolleg.notification.model.Notification;
import ru.itcolleg.notification.repository.NotificationRepository;
import ru.itcolleg.user.exception.UserNotFoundException;
import ru.itcolleg.user.model.User;
import ru.itcolleg.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с уведомлениями.
 * Implementation of the service for managing notifications.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final UserService userService;
    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(UserService userService, NotificationRepository notificationRepository) {
        this.userService = userService;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> getNotificationsByUserId(Long userId) {
        logger.info("Получение уведомлений для пользователя с ID: {}", userId);
        if (userId == null) {
            logger.warn("Идентификатор пользователя не указан.");
            return Collections.emptyList();
        }
        return notificationRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public Notification saveNotification(Long userId, String message) throws UserNotFoundException {
        logger.info("Сохранение уведомления для пользователя с ID: {}", userId);
        if (userId == null) {
            logger.warn("Идентификатор пользователя не указан.");
            throw new IllegalArgumentException("User ID cannot be null.");
        }

        User user = getUserById(userId);
        Notification notification = createNotification(user, message);
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void deleteNotifications(Long userId) {
        logger.info("Удаление всех уведомлений для пользователя с ID: {}", userId);
        if (userId == null) {
            logger.warn("Идентификатор пользователя не указан.");
            throw new IllegalArgumentException("User ID cannot be null.");
        }
        notificationRepository.deleteAllByUserId(userId);
    }

    private User getUserById(Long userId) throws UserNotFoundException {
        Optional<User> optionalUser = userService.getUserById(userId);
        return optionalUser.orElseThrow(() -> {
            logger.error("Пользователь с ID {} не найден", userId);
            return new UserNotFoundException("User not found for ID: " + userId);
        });
    }

    private Notification createNotification(User user, String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUserId(user.getId());
        return notification;
    }
}
