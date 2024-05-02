package ru.itcolleg.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import ru.itcolleg.auth.service.RequiresTokenValidation;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.notification.model.Notification;
import ru.itcolleg.notification.service.NotificationService;
import ru.itcolleg.transaction.dto.TransactionDTO;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationRestController {

    private final SimpMessagingTemplate messagingTemplate;

    private final NotificationService notificationService;
    private final TokenService tokenService;

    @Autowired
    public NotificationRestController(SimpMessagingTemplate messagingTemplate, NotificationService notificationService, TokenService tokenService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
        this.tokenService = tokenService;
    }

    @PostMapping("/send-message")
    public ResponseEntity<String> sendMessageToWebSocketClients(@RequestBody String message) {
        messagingTemplate.convertAndSend("/topic/messages", message);
        return ResponseEntity.ok("Message sent to WebSocket clients");
    }

    @GetMapping
    public ResponseEntity<?> getNotificationsByUserId(@RequestHeader("Authorization") String token) {
        try {
            Long userId = tokenService.extractUserIdFromToken(token);
            List<Notification> userNotifications = notificationService.getNotificationsByUserId(userId);
            return ResponseEntity.ok(userNotifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
