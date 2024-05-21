package ru.itcolleg.goal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.goal.dto.FinancialGoalDTO;
import ru.itcolleg.goal.exception.GoalNotFoundException;
import ru.itcolleg.goal.exception.TokenNotFoundException;
import ru.itcolleg.goal.service.FinancialGoalService;

import java.util.List;

/**
 * Контроллер для управления финансовыми целями.
 */
@RestController
@RequestMapping("/api/goals")
public class FinancialGoalController {
    private static final Logger logger = LoggerFactory.getLogger(FinancialGoalController.class);
    private final FinancialGoalService service;
    private final TokenService tokenService;

    public FinancialGoalController(FinancialGoalService service, TokenService tokenService) {
        this.service = service;
        this.tokenService = tokenService;
    }

    /**
     * Получить все финансовые цели пользователя.
     */
    @GetMapping
    public ResponseEntity<?> getAllGoals(@RequestHeader("Authorization") String token) {
        logger.info("Получение всех целей.");
        try {
            Long userId = tokenService.extractUserIdFromToken(token);
            List<FinancialGoalDTO> userGoals = service.getAllUserGoals(userId);
            return ResponseEntity.ok(userGoals);
        } catch (TokenNotFoundException e) {
            logger.error("Ошибка при получении всех целей: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Токен не найден");
        } catch (Exception e) {
            logger.error("Ошибка при получении всех целей: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при получении всех целей");
        }
    }

    /**
     * Получить финансовую цель по ее идентификатору.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getGoalById(@PathVariable Long id) {
        logger.info("Получение цели по ID: {}", id);
        try {
            FinancialGoalDTO goalDTO = service.getGoalById(id);
            return goalDTO != null ? ResponseEntity.ok(goalDTO) : ResponseEntity.notFound().build();
        } catch (GoalNotFoundException e) {
            logger.error("Цель с ID {} не найдена: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Ошибка при получении цели по ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при получении цели по ID");
        }
    }

    /**
     * Создать новую финансовую цель.
     */
    @PostMapping
    public ResponseEntity<?> createGoal(@RequestBody FinancialGoalDTO goalDTO, @RequestHeader("Authorization") String token) {
        logger.info("Создание новой цели.");
        try {
            Long userId = tokenService.extractUserIdFromToken(token);
            FinancialGoalDTO createdGoalDTO = service.createGoalForUser(goalDTO, userId);
            return new ResponseEntity<>(createdGoalDTO, HttpStatus.CREATED);
        } catch (TokenNotFoundException e) {
            logger.error("Ошибка при создании новой цели: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Токен не найден");
        } catch (Exception e) {
            logger.error("Ошибка при создании новой цели: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при создании новой цели");
        }
    }

    /**
     * Обновить существующую финансовую цель.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGoal(@PathVariable Long id, @RequestBody FinancialGoalDTO goalDTO) {
        logger.info("Обновление цели с ID: {}", id);
        try {
            FinancialGoalDTO updatedGoalDTO = service.updateGoal(id, goalDTO);
            return updatedGoalDTO != null ? ResponseEntity.ok(updatedGoalDTO) : ResponseEntity.notFound().build();
        } catch (GoalNotFoundException e) {
            logger.error("Цель с ID {} не найдена: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Ошибка при обновлении цели с ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обновлении цели с ID");
        }
    }

    /**
     * Удалить финансовую цель по ее идентификатору.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGoal(@PathVariable Long id) {
        logger.info("Удаление цели с ID: {}", id);
        try {
            service.deleteGoalById(id);
            return ResponseEntity.noContent().build();
        } catch (GoalNotFoundException e) {
            logger.error("Цель с ID {} не найдена: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Ошибка при удалении цели с ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении цели с ID");
        }
    }
}
