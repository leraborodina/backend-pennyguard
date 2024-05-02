package ru.itcolleg.transaction.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.notification.model.Notification;
import ru.itcolleg.notification.service.NotificationService;
import ru.itcolleg.transaction.dto.TransactionLimitDTO;
import ru.itcolleg.transaction.model.Category;
import ru.itcolleg.transaction.service.CategoryService;
import ru.itcolleg.transaction.service.TransactionLimitService;
import ru.itcolleg.transaction.service.TransactionService;

import java.util.List;

@Component
public class TransactionLimitNotificationScheduler extends TextWebSocketHandler {

    @Autowired
    private TransactionLimitService limitService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RestTemplate restTemplate;

    //@Scheduled(cron = "0 */1 * * * *")
    public void checkLimitsAndSendNotifications() {
        try {
            String token = this.tokenService.getToken();
            Long currentUserId = this.tokenService.extractUserIdFromToken(token);

            List<TransactionLimitDTO> userLimits = limitService.getTransactionLimitsByUserId(currentUserId);
            userLimits.forEach(limit -> processLimit(currentUserId, limit));
        } catch (Exception e) {
            handleException("Error processing daily limit check: ", e);
        }
    }

    private void processLimit(Long currentUserId, TransactionLimitDTO userLimit) {
        String category = getCategoryName(userLimit.getCategoryId());
        double currentExpenses = transactionService.getUserFixExpenses(currentUserId, userLimit.getCategoryId(), userLimit.getSalaryDay());
        if (currentExpenses >= userLimit.getAmount() * 0.8) {
            sendNotification(currentUserId, userLimit, category);
        }
    }

    private String getCategoryName(Long categoryId) {
        return categoryId != null ? categoryService.getById(categoryId)
                .map(Category::getName)
                .orElse(null) : null;
    }

    private void sendNotification(Long currentUserId, TransactionLimitDTO userLimit, String category) {
        String message = buildNotificationMessage(userLimit, category);
        notificationService.saveNotification(currentUserId, message);
        sendMessageToWebSocket(message);
    }

    private String buildNotificationMessage(TransactionLimitDTO userLimit, String category) {
        String categoryMessage = category != null ? " category " + category : " all expenses";
        return "Your limit for" + categoryMessage + " has reached 80%.";
    }

    private void sendMessageToWebSocket(String message) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(message, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8080/api/notifications/send-message", HttpMethod.POST, requestEntity, String.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                System.out.println("Message sent successfully to WebSocket clients");
            } else {
                System.err.println("Failed to send message to WebSocket clients. Status code: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            handleException("Error sending message to WebSocket clients: ", e);
        }
    }

    private void handleException(String prefix, Exception e) {
        System.err.println(prefix + e.getMessage());
    }
}
