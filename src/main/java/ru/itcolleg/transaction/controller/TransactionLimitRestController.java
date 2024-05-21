package ru.itcolleg.transaction.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itcolleg.auth.aspect.RequiresTokenValidation;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.transaction.dto.CategoryLimitDTO;
import ru.itcolleg.transaction.service.CategoryLimitService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * REST контроллер для управления лимитами транзакций.
 */
@RestController
@RequestMapping("/api/transaction-limits")
public class TransactionLimitRestController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionLimitRestController.class);

    private final CategoryLimitService categoryLimitService;
    private final TokenService tokenService;

    @Autowired
    public TransactionLimitRestController(CategoryLimitService categoryLimitService, TokenService tokenService) {
        this.categoryLimitService = categoryLimitService;
        this.tokenService = tokenService;
    }

    /**
     * Получает лимиты транзакций по ID пользователя.
     *
     * @param token Токен авторизации.
     * @return ResponseEntity, содержащий список лимитов транзакций.
     */
    @GetMapping
    public ResponseEntity<?> getTransactionLimitsByUserId(@RequestHeader("Authorization") String token) {
        logger.info("Получение лимитов транзакций по ID пользователя");
        try {
            Long userId = tokenService.extractUserIdFromToken(token);
            List<CategoryLimitDTO> transactionLimits = categoryLimitService.getCategoryLimitsByUserId(userId);
            return ResponseEntity.ok(transactionLimits);
        } catch (DataAccessException e) {
            logger.error("Ошибка базы данных при получении лимитов транзакций", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка базы данных");
        } catch (Exception e) {
            logger.error("Ошибка при получении лимитов транзакций", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
        }
    }

    /**
     * Получает лимит транзакции по его ID.
     *
     * @param id    ID лимита транзакции.
     * @param token Токен авторизации.
     * @return ResponseEntity, содержащий лимит транзакции, если найден, в противном случае возвращает 404 Not Found.
     */
    @RequiresTokenValidation
    @GetMapping("/{id}")
    public ResponseEntity<CategoryLimitDTO> getTransactionLimitById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        logger.info("Получение лимита транзакции по ID: {}", id);
        try {
            CategoryLimitDTO limitDTO = categoryLimitService.getCategoryLimitById(id);
            return ResponseEntity.ok(limitDTO);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Создает новый лимит транзакции.
     *
     * @param limitDTO Лимит транзакции для создания.
     * @param token    Токен авторизации.
     * @return ResponseEntity с HTTP-статусом 201 (Created) в случае успеха, в противном случае возвращает статус ошибки.
     */
    @RequiresTokenValidation
    @PostMapping
    public ResponseEntity<Void> createTransactionLimit(@RequestBody CategoryLimitDTO limitDTO, @RequestHeader("Authorization") String token) {
        logger.info("Создание лимита транзакции: {}", limitDTO);
        try {
            Long extractedUserId = tokenService.extractUserIdFromToken(token);
            categoryLimitService.createCategoryLimit(limitDTO, extractedUserId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Обновляет существующий лимит транзакции.
     *
     * @param id       ID лимита транзакции для обновления.
     * @param limitDTO Обновленный лимит транзакции.
     * @param token    Токен авторизации.
     * @return ResponseEntity с HTTP-статусом 200 (OK) в случае успеха, в противном случае возвращает статус ошибки.
     */
    @RequiresTokenValidation
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTransactionLimit(@PathVariable Long id, @RequestBody CategoryLimitDTO limitDTO, @RequestHeader("Authorization") String token) {
        logger.info("Обновление лимита транзакции с ID {}: {}", id, limitDTO);
        try {
            if (!id.equals(limitDTO.getId())) {
                return ResponseEntity.badRequest().build();
            }
            categoryLimitService.updateCategoryLimit(limitDTO);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Удаляет лимит транзакции по его ID.
     *
     * @param id    ID лимита транзакции для удаления.
     * @param token Токен авторизации.
     * @return ResponseEntity с HTTP-статусом 200 (OK) в случае успеха, в противном случае возвращает статус ошибки.
     */
    @RequiresTokenValidation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionLimit(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        logger.info("Удаление лимита транзакции с ID: {}", id);
        try {
            categoryLimitService.deleteCategoryLimit(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
