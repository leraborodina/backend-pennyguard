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
import ru.itcolleg.transaction.model.Category;
import ru.itcolleg.transaction.service.CategoryService;

import java.util.List;
import java.util.Optional;

/**
 * REST контроллер для управления категориями.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryRestController.class);
    private final CategoryService categoryService;
    private final TokenService tokenService;

    @Autowired
    public CategoryRestController(CategoryService categoryService, TokenService tokenService) {
        this.categoryService = categoryService;
        this.tokenService = tokenService;
    }

    /**
     * Получает категорию по ее ID.
     *
     * @param id    ID категории.
     * @param token Токен авторизации.
     * @return ResponseEntity, содержащий категорию, если она найдена, в противном случае возвращает 404 Not Found.
     */
    @RequiresTokenValidation
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        logger.info("Получение категории по ID: {}", id);
        try {
            Optional<Category> category = categoryService.getById(id);
            return category.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Произошла ошибка при получении категории по ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
        }
    }

    /**
     * Удаляет категорию по ее ID.
     *
     * @param id    ID категории.
     * @param token Токен авторизации.
     * @return ResponseEntity без содержимого, если удаление прошло успешно, в противном случае возвращает 404 Not Found.
     */
    @RequiresTokenValidation
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        logger.info("Удаление категории по ID: {}", id);
        try {
            categoryService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Произошла ошибка при удалении категории по ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
        }
    }

    /**
     * Получает все категории.
     *
     * @param token Токен авторизации.
     * @return ResponseEntity, содержащий список категорий.
     */
    @GetMapping
    public ResponseEntity<?> getAllCategories(@RequestHeader("Authorization") String token) {
        logger.info("Получение всех категорий");
        try {
            Long userId = tokenService.extractUserIdFromToken(token);
            List<Category> categories = categoryService.getAll(userId);
            return ResponseEntity.ok(categories);
        } catch (DataAccessException e) {
            logger.error("Ошибка базы данных при получении всех категорий", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка базы данных");
        } catch (Exception e) {
            logger.error("Произошла ошибка при получении всех категорий", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
        }
    }

    /**
     * Сохраняет новую категорию.
     *
     * @param category Категория, которую нужно сохранить.
     * @param token    Токен авторизации.
     * @return ResponseEntity с сохраненной категорией в случае успеха, в противном случае возвращает 400 Bad Request.
     */
    @RequiresTokenValidation
    @PostMapping
    public ResponseEntity<?> saveCategory(@RequestBody Category category, @RequestHeader("Authorization") String token) {
        logger.info("Сохранение категории: {}", category);
        try {
            Optional<Category> savedCategory = categoryService.save(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Произошла ошибка при сохранении категории: {}", category, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
        }
    }

    /**
     * Обновляет существующую категорию.
     *
     * @param category Обновленная категория.
     * @param id       ID категории для обновления.
     * @param token    Токен авторизации.
     * @return ResponseEntity с обновленной категорией в случае успеха, в противном случае возвращает 404 Not Found.
     */
    @RequiresTokenValidation
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@RequestBody Category category, @PathVariable Long id, @RequestHeader("Authorization") String token) {
        logger.info("Обновление категории с ID {}: {}", id, category);
        try {
            Optional<Category> updatedCategory = categoryService.update(category, id);
            return updatedCategory.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Произошла ошибка при обновлении категории с ID {}: {}", id, category, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
        }
    }
}
