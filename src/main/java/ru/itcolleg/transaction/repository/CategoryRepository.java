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
    List<Category> findByIsDefaultFalseAndUserId(Long userId);

    /**
     * Checks if a category with a given name exists for a specific user.
     * Проверяет, существует ли категория с заданным именем для указанного пользователя.
     *
     * @param name The name of the category
     * @param userId The ID of the user
     * @return true if the category exists, false otherwise
     */
    boolean existsByNameAndUserId(String name, Long userId);

    /**
     * Checks if a category with a given name exists for a specific user and is marked as a default category.
     * Проверяет, существует ли категория с заданным именем для указанного пользователя и отмечена как категория по умолчанию.
     *
     * @param name The name of the category
     * @param userId The ID of the user
     * @param isDefault true if the category is default, false otherwise
     * @return true if the category exists and is default, false otherwise
     */
    boolean existsByNameAndUserIdAndIsDefault(String name, Long userId, boolean isDefault);
}
