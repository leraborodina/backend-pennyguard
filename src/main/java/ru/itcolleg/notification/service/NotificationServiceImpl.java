package ru.itcolleg.notification.service;

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

@Service
public class NotificationServiceImpl implements NotificationService {

    private final UserService userService;
    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(UserService userService, NotificationRepository notificationRepository) {
        this.userService = userService;
        this.notificationRepository = notificationRepository;
    }


    @Override
    public List<Notification> getNotificationsByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }

        return this.notificationRepository.findAllByUserId(userId);
    }

    @Override
    public Notification saveNotification(Long userId, String message) {
        if (userId == null) {
            return null;
        }

        Optional<User> optionalUser = this.userService.getUserById(userId);

        if (optionalUser.isPresent()) {
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setUserId(userId);

            return this.notificationRepository.save(notification);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteNotifications(Long userId) {
        this.notificationRepository.deleteAllByUserId(userId);
    }
}