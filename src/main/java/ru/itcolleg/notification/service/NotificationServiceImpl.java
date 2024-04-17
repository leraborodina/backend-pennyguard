package ru.itcolleg.notification.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendNotification(Long userId, String message) {
        // Logic to send notification to the user with the specified userId
        System.out.println("Sending notification to user " + userId + ": " + message);

        // Send the notification message to the user's WebSocket topic
        messagingTemplate.convertAndSendToUser(String.valueOf(userId), "/topic/notifications", message);
    }
}