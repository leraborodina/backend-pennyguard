package ru.itcolleg.notification.model;

import javax.persistence.*;

/**
 * Model class representing a notification.
 * Класс модели, представляющий уведомление.
 */
@Entity
@Table(name = "[notification]", schema = "[dbo]")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "message", nullable = false)
    private String message;

    /**
     * Default constructor.
     * Конструктор по умолчанию.
     */
    public Notification() {
        // Default constructor
    }

    /**
     * Get the ID of the notification.
     * Получить идентификатор уведомления.
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the ID of the notification.
     * Установить идентификатор уведомления.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the ID of the user associated with the notification.
     * Получить идентификатор пользователя, связанного с уведомлением.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Set the ID of the user associated with the notification.
     * Установить идентификатор пользователя, связанного с уведомлением.
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Get the message content of the notification.
     * Получить содержание сообщения уведомления.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message content of the notification.
     * Установить содержание сообщения уведомления.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
