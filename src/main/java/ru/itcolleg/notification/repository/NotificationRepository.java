package ru.itcolleg.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itcolleg.notification.model.Notification;

import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}
