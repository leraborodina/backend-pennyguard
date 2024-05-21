package ru.itcolleg.goal.service;

import ru.itcolleg.goal.dto.FinancialGoalDTO;

import java.util.List;

/**
 * Сервис для работы с финансовыми целями.
 * Service for working with financial goals.
 */
public interface FinancialGoalService {

    /**
     * Получить все финансовые цели пользователя.
     * Get all financial goals of a user.
     *
     * @param userId Идентификатор пользователя
     *               User ID
     * @return Список финансовых целей пользователя
     * List of financial goals of the user
     */
    List<FinancialGoalDTO> getAllUserGoals(Long userId);

    /**
     * Получить финансовую цель по ее идентификатору.
     * Get a financial goal by its ID.
     *
     * @param id Идентификатор цели
     *           Goal ID
     * @return DTO объект финансовой цели
     * DTO object of the financial goal
     */
    FinancialGoalDTO getGoalById(Long id);

    /**
     * Создать новую финансовую цель для пользователя.
     * Create a new financial goal for a user.
     *
     * @param goalDTO DTO объект финансовой цели
     *                DTO object of the financial goal
     * @param userId  Идентификатор пользователя
     *                User ID
     * @return DTO объект созданной финансовой цели
     * DTO object of the created financial goal
     */
    FinancialGoalDTO createGoalForUser(FinancialGoalDTO goalDTO, Long userId);

    /**
     * Обновить существующую финансовую цель.
     * Update an existing financial goal.
     *
     * @param id      Идентификатор цели
     *                Goal ID
     * @param goalDTO DTO объект финансовой цели
     *                DTO object of the financial goal
     * @return DTO объект обновленной финансовой цели
     * DTO object of the updated financial goal
     */
    FinancialGoalDTO updateGoal(Long id, FinancialGoalDTO goalDTO);

    /**
     * Удалить финансовую цель по ее идентификатору.
     * Delete a financial goal by its ID.
     *
     * @param id Идентификатор цели
     *           Goal ID
     */
    void deleteGoalById(Long id);
}
