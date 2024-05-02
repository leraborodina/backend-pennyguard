package ru.itcolleg.transaction.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.notification.service.NotificationService;

public class NotificationsScheduler {

    private final TokenService tokenService;
    private final NotificationService notificationService;

    public NotificationsScheduler(TokenService tokenService, NotificationService notificationService) {
        this.tokenService = tokenService;
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkLimitsAndSendNotifications() {
        try {
            String token = this.tokenService.getToken();
            Long currentUserId = this.tokenService.extractUserIdFromToken(token);

            this.notificationService.getNotificationsByUserId(currentUserId);
        } catch (Exception e) {
            handleException("Error processing daily limit check: ", e);
        }
    }

    private void handleException(String prefix, Exception e) {
        System.err.println(prefix + e.getMessage());
    }
}
