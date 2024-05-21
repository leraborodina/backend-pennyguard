package ru.itcolleg.notification.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.notification.model.Notification;
import ru.itcolleg.notification.service.NotificationService;

import java.util.List;

/**
 * Контроллер для управления уведомлениями.
 * Controller for managing notifications.
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationRestController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationRestController.class);

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;
    private final TokenService tokenService;

    @Autowired
    public NotificationRestController(SimpMessagingTemplate messagingTemplate, NotificationService notificationService, TokenService tokenService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
        this.tokenService = tokenService;
    }

    /**
     * Отправить сообщение клиентам WebSocket.
     * Send a message to WebSocket clients.
     */
    @PostMapping("/send-message")
    public ResponseEntity<String> sendMessageToWebSocketClients(@RequestBody String message) {
        logger.info("Отправка сообщения клиентам WebSocket.");
        messagingTemplate.convertAndSend("/notifications", message);
        return ResponseEntity.ok("Сообщение отправлено клиентам WebSocket");
    }

    /**
     * Получить уведомления по идентификатору пользователя.
     * Get notifications by user ID.
     */
    @GetMapping
    public ResponseEntity<?> getNotificationsByUserId(@RequestHeader("Authorization") String token) {
        logger.info("Получение уведомлений по идентификатору пользователя.");
        try {
            Long userId = tokenService.extractUserIdFromToken(token);
            List<Notification> userNotifications = notificationService.getNotificationsByUserId(userId);
            return ResponseEntity.ok(userNotifications);
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка при извлечении идентификатора пользователя из токена: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при извлечении идентификатора пользователя из токена");
        } catch (Exception e) {
            logger.error("Ошибка при получении уведомлений: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при получении уведомлений");
        }
    }
}
