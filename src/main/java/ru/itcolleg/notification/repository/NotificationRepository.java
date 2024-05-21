package ru.itcolleg.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itcolleg.notification.model.Notification;

import java.util.List;

/**
 * Репозиторий для работы с уведомлениями.
 * Repository for working with notifications.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Найти все уведомления по идентификатору пользователя.
     * Find all notifications by user ID.
     *
     * @param userId Идентификатор пользователя
     * @return Список уведомлений
     */
    List<Notification> findAllByUserId(Long userId);

    /**
     * Удалить все уведомления по идентификатору пользователя.
     * Delete all notifications by user ID.
     *
     * @param userId Идентификатор пользователя
     */
    void deleteAllByUserId(Long userId);
}
