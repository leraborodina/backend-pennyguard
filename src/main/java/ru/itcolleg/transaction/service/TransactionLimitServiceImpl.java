package ru.itcolleg.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itcolleg.notification.service.NotificationService;
import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.dto.TransactionLimitDTO;
import ru.itcolleg.transaction.mapper.TransactionLimitMapper;
import ru.itcolleg.transaction.model.Category;
import ru.itcolleg.transaction.model.TransactionLimit;
import ru.itcolleg.transaction.repository.TransactionLimitRepository;

import javax.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TransactionLimitServiceImpl implements TransactionLimitService {

    private final TransactionLimitRepository transactionLimitRepository;
    private final TransactionLimitMapper transactionLimitMapper;
    private final CategoryService categoryService;
    private final RestTemplate restTemplate;

    private final NotificationService notificationService;

    public TransactionLimitServiceImpl(TransactionLimitRepository transactionLimitRepository, TransactionLimitMapper transactionLimitMapper, CategoryService categoryService, RestTemplate restTemplate, NotificationService notificationService) {
        this.transactionLimitRepository = transactionLimitRepository;
        this.transactionLimitMapper = transactionLimitMapper;
        this.categoryService = categoryService;
        this.restTemplate = restTemplate;
        this.notificationService = notificationService;
    }

    @Override
    public void checkLimitsAndSendNotifications(Long userId, List<TransactionDTO> currentExpences) {
        try {
            List<TransactionLimitDTO> userLimits = this.getTransactionLimitsByUserId(userId);
            userLimits.forEach(limit -> processLimit(userId, limit, currentExpences));
        } catch (Exception e) {
            handleException("Error processing daily limit check: ", e);
        }
    }

    private void processLimit(Long currentUserId, TransactionLimitDTO userLimit, List<TransactionDTO> currentExpences) {
        String category = getCategoryName(userLimit.getCategoryId());
        List<TransactionDTO> expences = currentExpences; // all user expences

        // Get current year and month
        OffsetDateTime currentDateTime = OffsetDateTime.now();
        int currentYear = currentDateTime.getYear();
        int currentMonth = currentDateTime.getMonthValue();

        // Get salary day from user limits
        int startDay = userLimit.getSalaryDay();

        // Set start date to the salary day of the current month
        OffsetDateTime startDate = OffsetDateTime.of(currentYear, currentMonth, startDay, 0, 0, 0, 0, ZoneOffset.UTC);

        // Set end date to the last day of the current month
        OffsetDateTime endDate = startDate.plusMonths(1).minusDays(1).withHour(23).withMinute(59).withSecond(59);

        double sum = expences.stream().filter(
                        transactionDTO -> {
                            boolean isWithinDateRange = transactionDTO.getCreatedAt().isAfter(startDate) && transactionDTO.getCreatedAt().isBefore(endDate);

                            return (userLimit.getCategoryId() == null ||
                                    isWithinDateRange &&
                                            (transactionDTO.getCategoryId() == null || Objects.equals(transactionDTO.getCategoryId(), userLimit.getCategoryId())));
                        }
                )
                .mapToDouble(TransactionDTO::getAmount)
                .sum();


        double currentExpenses = sum;
        if (currentExpenses >= userLimit.getAmount() * 0.8) {
            sendNotification(currentUserId, userLimit, category);
        }
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

    private String getCategoryName(Long categoryId) {
        return categoryId != null ? categoryService.getById(categoryId)
                .map(Category::getName)
                .orElse(null) : null;
    }

    @Override
    public TransactionLimitDTO getTransactionLimitById(Long id) {
        validateId(id);
        Optional<TransactionLimit> limitOptional = transactionLimitRepository.findById(id);
        return limitOptional.map(transactionLimitMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Transaction limit not found with id: " + id));
    }

    @Override
    public void createTransactionLimit(TransactionLimitDTO limitDTO, Long userId) {
        validateLimitDTO(limitDTO);
        try {
            TransactionLimit limit = transactionLimitMapper.toEntity(limitDTO);
            limit.setUserId(userId);
            transactionLimitRepository.save(limit);
        } catch (Exception e) {
            handleException("Failed to set transaction limit", e);
        }
    }

    @Override
    public void updateTransactionLimit(TransactionLimitDTO limitDTO) {
        validateLimitDTO(limitDTO);
        try {
            TransactionLimit limit = transactionLimitMapper.toEntity(limitDTO);
            transactionLimitRepository.save(limit);
        } catch (Exception e) {
            handleException("Failed to update transaction limit", e);
        }
    }

    @Override
    public List<TransactionLimitDTO> getTransactionLimitsByUserId(Long userId) {

        if (userId == null) {
            return Collections.emptyList();
        }

        List<TransactionLimit> foundLimits = transactionLimitRepository.findByUserId(userId);

        return foundLimits.stream()
                .map(transactionLimitMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTransactionLimit(Long id) {
        validateId(id);
        try {
            transactionLimitRepository.deleteById(id);
        } catch (Exception e) {
            handleException("Failed to delete transaction limit", e);
        }
    }

    private <T> List<T> iterableToList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
    }

    private void validateLimitDTO(TransactionLimitDTO limitDTO) {
        if (limitDTO == null) {
            throw new IllegalArgumentException("Transaction limit DTO cannot be null");
        }
    }

    private void handleException(String message, Exception e) {
        throw new RuntimeException(message + ": " + e.getMessage(), e);
    }
}
