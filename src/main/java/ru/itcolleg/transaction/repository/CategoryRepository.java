package ru.itcolleg.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itcolleg.transaction.model.Category;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing Category entities.
 * Репозиторий для управления сущностями Category.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Finds a category by name containing the specified value (case-insensitive).
     * Находит категорию по имени, содержащему указанное значение (без учета регистра).
     *
     * @param value The value to search for
     * @return An Optional containing the found category, or empty if not found
     */
    Optional<Category> findByNameContainingIgnoreCase(String value);

    /**
     * Finds all default categories.
     * Находит все категории по умолчанию.
     *
     * @return List of default categories
     */
    List<Category> findByIsDefaultTrue();

    /**
     * Finds all default categories or categories owned by the specified user.
     * Находит все категории по умолчанию или категории, принадлежащие указанному пользователю.
     *
     * @param userId The ID of the user
     * @return List of categories
     */
    List<Category> findByIsDefaultTrueOrUserId(Long userId);
}
