package ru.itcolleg.goal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itcolleg.goal.model.FinancialGoal;

import java.util.List;

/**
 * Репозиторий для работы с финансовыми целями.
 * Repository for working with financial goals.
 */
public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, Long> {

    /**
     * Найти все цели пользователя по идентификатору пользователя.
     * Find all goals of a user by user ID.
     *
     * @param userId Идентификатор пользователя
     *               User ID
     * @return Список финансовых целей пользователя
     * List of financial goals of the user
     */
    List<FinancialGoal> findGoalsByUserId(Long userId);
}
