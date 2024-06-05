package ru.itcolleg.transaction.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itcolleg.notification.service.NotificationService;
import ru.itcolleg.transaction.dto.CategoryLimitDTO;
import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.mapper.CategoryLimitMapper;
import ru.itcolleg.transaction.model.Category;
import ru.itcolleg.transaction.model.CategoryLimit;
import ru.itcolleg.transaction.repository.TransactionLimitRepository;
import ru.itcolleg.user.exception.UserNotFoundException;
import java.time.format.DateTimeFormatter;


import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for managing category limits.
 * Реализация сервиса для управления лимитами категорий.
 */
@Service
public class CategoryLimitServiceImpl implements CategoryLimitService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryLimitServiceImpl.class);

    private final TransactionLimitRepository transactionLimitRepository;
    private final CategoryLimitMapper categoryLimitMapper;
    private final CategoryService categoryService;
    private final RestTemplate restTemplate;

    private final NotificationService notificationService;

    @Autowired
    public CategoryLimitServiceImpl(TransactionLimitRepository transactionLimitRepository,
                                    CategoryLimitMapper categoryLimitMapper,
                                    CategoryService categoryService,
                                    RestTemplate restTemplate,
                                    NotificationService notificationService) {
        this.transactionLimitRepository = transactionLimitRepository;
        this.categoryLimitMapper = categoryLimitMapper;
        this.categoryService = categoryService;
        this.restTemplate = restTemplate;
        this.notificationService = notificationService;
    }

    @Override
    public void checkLimitsAndSendNotifications(Long userId, List<TransactionDTO> currentExpenses) {
        try {
            List<CategoryLimitDTO> userLimits = this.getCategoryLimitsByUserId(userId);
            userLimits.forEach(limit -> {
                try {
                    processLimit(userId, limit, currentExpenses);
                } catch (UserNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            handleException("Ошибка при обработке ежедневной проверки лимита: ", e);
        }
    }

    private void processLimit(Long currentUserId, CategoryLimitDTO userLimit, List<TransactionDTO> currentExpenses) throws UserNotFoundException {
        String category = getCategoryName(userLimit.getCategoryId());
        List<TransactionDTO> expenses = currentExpenses;

        LocalDateTime currentDateTime = LocalDateTime.now();
        int currentYear = currentDateTime.getYear();
        int currentMonth = currentDateTime.getMonthValue();

        int startDay = userLimit.getStartDay();

        LocalDateTime startDate = LocalDateTime.of(currentYear, currentMonth, startDay, 0, 0, 0);

        LocalDateTime endDate = startDate.plusMonths(1).minusDays(1).withHour(23).withMinute(59).withSecond(59);

        double sum = expenses.stream().filter(
                        transactionDTO -> {
                            LocalDateTime transactionDateTime = LocalDateTime.parse(transactionDTO.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                            boolean isWithinDateRange = transactionDateTime.isAfter(startDate) && transactionDateTime.isBefore(endDate);

                            return (userLimit.getCategoryId() == null ||
                                    isWithinDateRange &&
                                            (transactionDTO.getCategoryId() == null || Objects.equals(transactionDTO.getCategoryId(), userLimit.getCategoryId())));
                        }
                )
                .mapToDouble(TransactionDTO::getAmount)
                .sum();


        if (sum >= userLimit.getAmount() * 0.8) {
            sendNotification(currentUserId, userLimit, category);
        }
    }

    private void sendNotification(Long currentUserId, CategoryLimitDTO userLimit, String category) throws UserNotFoundException {
        String message = buildNotificationMessage(userLimit, category);
        notificationService.saveNotification(currentUserId, message);
        sendMessageToWebSocket(message);
    }

    private String buildNotificationMessage(CategoryLimitDTO userLimit, String category) {
        String categoryMessage = category != null ? " категории " + category : " всех расходов";
        return "Ваш лимит для" + categoryMessage + " достиг 80%.";
    }

    private void sendMessageToWebSocket(String message) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(message, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:8080/api/notifications/send-message", HttpMethod.POST, requestEntity, String.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                logger.info("Сообщение успешно отправлено клиентам WebSocket");
            } else {
                logger.error("Не удалось отправить сообщение клиентам WebSocket. Код состояния: {}", responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            handleException("Ошибка отправки сообщения клиентам WebSocket: ", e);
        }
    }

    private String getCategoryName(Long categoryId) {
        return categoryId != null ? categoryService.getById(categoryId)
                .map(Category::getName)
                .orElse(null) : null;
    }

    @Override
    public CategoryLimitDTO getCategoryLimitById(Long id) {
        validateId(id);
        Optional<CategoryLimit> limitOptional = transactionLimitRepository.findById(id);
        return limitOptional.map(categoryLimitMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Предел транзакции с идентификатором " + id + " не найден"));
    }

    @Override
    public void createCategoryLimit(CategoryLimitDTO limitDTO, Long userId) {
        validateLimitDTO(limitDTO);
        try {
            CategoryLimit limit = categoryLimitMapper.toEntity(limitDTO);
            limit.setUserId(userId);
            transactionLimitRepository.save(limit);
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка при сохранении лимита: {}", e.getMessage());
            throw e; // Re-throw the exception to be handled by the controller
        } catch (DataIntegrityViolationException e) {
            logger.error("Ошибка при сохранении лимита: {}", e.getMessage());
            throw new DuplicateKeyException("Лимит уже существует", e);
        } catch (Exception e) {
            logger.error("Ошибка при сохранении лимита: {}", e.getMessage());
            throw new RuntimeException("Ошибка при сохранении категории", e);
        }
    }

    @Override
    public void updateCategoryLimit(CategoryLimitDTO limitDTO, Long userId) {
        validateLimitDTO(limitDTO);
        try {
            CategoryLimit limit = categoryLimitMapper.toEntity(limitDTO);
            limit.setUserId(userId);
            transactionLimitRepository.save(limit);
        } catch (Exception e) {
            handleException("Не удалось обновить предел транзакции", e);
        }
    }

    @Override
    public List<CategoryLimitDTO> getCategoryLimitsByUserId(Long userId) {

        if (userId == null) {
            return Collections.emptyList();
        }

        List<CategoryLimit> foundLimits = transactionLimitRepository.findByUserId(userId);

        return foundLimits.stream()
                .map(categoryLimitMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategoryLimit(Long id) {
        validateId(id);
        try {
            transactionLimitRepository.deleteById(id);
        } catch (Exception e) {
            handleException("Не удалось удалить предел транзакции", e);
        }
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Идентификатор не может быть пустым");
        }
    }

    private void validateLimitDTO(CategoryLimitDTO limitDTO) {
        if (limitDTO == null) {
            throw new IllegalArgumentException("DTO предела транзакции не может быть пустым");
        }
    }

    private void handleException(String message, Exception e) {
        logger.error(message + ": " + e.getMessage(), e);
        throw new RuntimeException(message + ": " + e.getMessage(), e);
    }
}
