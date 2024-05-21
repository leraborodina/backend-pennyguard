package ru.itcolleg.transaction.service;

import ru.itcolleg.transaction.dto.CategoryLimitDTO;
import ru.itcolleg.transaction.dto.TransactionDTO;

import java.util.List;

/**
 * Interface for managing category limits.
 * Интерфейс для управления лимитами категорий.
 */
public interface CategoryLimitService {

    /**
     * Retrieves category limits by user ID.
     * Получает лимиты категорий по идентификатору пользователя.
     *
     * @param userId User ID.
     * @return List of category limits.
     * @return Список лимитов категорий.
     */
    List<CategoryLimitDTO> getCategoryLimitsByUserId(Long userId);

    /**
     * Retrieves a category limit by its ID.
     * Получает лимит категории по его идентификатору.
     *
     * @param id Category limit ID.
     * @return Category limit DTO.
     * @return DTO лимита категории.
     */
    CategoryLimitDTO getCategoryLimitById(Long id);

    /**
     * Creates a new category limit for a user.
     * Создает новый лимит категории для пользователя.
     *
     * @param limitDTO Category limit DTO.
     * @param userId   User ID.
     */
    void createCategoryLimit(CategoryLimitDTO limitDTO, Long userId);

    /**
     * Updates an existing category limit.
     * Обновляет существующий лимит категории.
     *
     * @param limitDTO Category limit DTO.
     */
    void updateCategoryLimit(CategoryLimitDTO limitDTO);

    /**
     * Deletes a category limit by its ID.
     * Удаляет лимит категории по его идентификатору.
     *
     * @param id Category limit ID.
     */
    void deleteCategoryLimit(Long id);

    /**
     * Checks category limits and sends notifications if exceeded.
     * Проверяет лимиты категорий и отправляет уведомления в случае превышения.
     *
     * @param userId          User ID.
     * @param currentExpenses List of current expenses.
     */
    void checkLimitsAndSendNotifications(Long userId, List<TransactionDTO> currentExpenses);
}
