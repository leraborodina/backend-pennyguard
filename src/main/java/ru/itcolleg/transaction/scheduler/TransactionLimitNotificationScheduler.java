package ru.itcolleg.transaction.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.itcolleg.notification.service.NotificationService;
import ru.itcolleg.transaction.dto.TransactionLimitDTO;
import ru.itcolleg.transaction.service.TransactionLimitService;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class TransactionLimitNotificationScheduler {

    private final TransactionLimitService transactionLimitService;
    private final NotificationService notificationService;

    public TransactionLimitNotificationScheduler(TransactionLimitService transactionLimitService, NotificationService notificationService) {
        this.transactionLimitService = transactionLimitService;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedRate = 86400000) // Check daily (adjust as needed)
    public void checkLimitsAndSendNotifications() {
        List<TransactionLimitDTO> limits = transactionLimitService.getAllTransactionLimits();
        OffsetDateTime currentDate = OffsetDateTime.now();

        for (TransactionLimitDTO limit : limits) {
            // Check if the limit will be exceeded within the next 3 days
            OffsetDateTime limitExpiryDate = calculateLimitExpiryDate(limit.getCreatedAt(), limit.getLimitType());
            if (currentDate.isAfter(limitExpiryDate)) {
                notificationService.sendNotification(limit.getUserId(), "Limit exceeded for category: " + limit.getCategoryId());
            }
        }
    }

    // Helper method to calculate the limit expiry date based on the limit type
    private OffsetDateTime calculateLimitExpiryDate(OffsetDateTime setDate, String limitType) {
        switch (limitType) {
            case "day":
                return setDate.plusDays(1);
            case "week":
                return setDate.plusWeeks(1);
            case "month":
                return setDate.plusMonths(1);
            default:
                throw new IllegalArgumentException("Invalid limit type: " + limitType);
        }
    }
}
