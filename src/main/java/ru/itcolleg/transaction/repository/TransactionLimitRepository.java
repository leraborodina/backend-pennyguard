package ru.itcolleg.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itcolleg.transaction.model.CategoryLimit;

import java.util.List;

/**
 * Repository for managing CategoryLimit entities.
 * Репозиторий для управления сущностями CategoryLimit.
 */
@Repository
public interface TransactionLimitRepository extends JpaRepository<CategoryLimit, Long> {

    /**
     * Finds category limits by user ID.
     * Находит ограничения категории по идентификатору пользователя.
     *
     * @param userId The ID of the user
     * @return List of category limits
     */
    List<CategoryLimit> findByUserId(Long userId);
}
