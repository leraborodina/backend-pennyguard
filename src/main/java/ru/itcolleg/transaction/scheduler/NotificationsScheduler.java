package ru.itcolleg.transaction.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.notification.service.NotificationService;

/**
 * Scheduler for sending notifications related to transaction limits.
 * Планировщик для отправки уведомлений, связанных с лимитами транзакций.
 */
public class NotificationsScheduler {

    private final TokenService tokenService;
    private final NotificationService notificationService;

    /**
     * Logger for NotificationsScheduler.
     * Логгер для NotificationsScheduler.
     */
    private static final Logger logger = LoggerFactory.getLogger(NotificationsScheduler.class);

    /**
     * Constructs a NotificationsScheduler with the specified dependencies.
     * Создает объект NotificationsScheduler с указанными зависимостями.
     *
     * @param tokenService        The token service
     * @param notificationService The notification service
     */
    public NotificationsScheduler(TokenService tokenService, NotificationService notificationService) {
        this.tokenService = tokenService;
        this.notificationService = notificationService;
    }

    /**
     * Checks limits and sends notifications daily.
     * Проверяет лимиты и отправляет уведомления ежедневно.
     */
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

    /**
     * Handles exceptions.
     * Обрабатывает исключения.
     *
     * @param prefix The prefix for the error message
     * @param e      The exception to handle
     */
    private void handleException(String prefix, Exception e) {
        logger.error(prefix, e);
    }
}
