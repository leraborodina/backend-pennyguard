package ru.itcolleg.transaction.service;

import ru.itcolleg.transaction.model.Category;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing categories.
 * Интерфейс сервиса для управления категориями.
 */
public interface CategoryService {

    /**
     * Saves a category.
     * Сохраняет категорию.
     *
     * @param category The category to save.
     * @return Optional containing the saved category.
     */
    Optional<Category> save(Category category, Long userId);

    /**
     * Retrieves a category by its ID.
     * Извлекает категорию по ее идентификатору.
     *
     * @param id The ID of the category.
     * @return Optional containing the retrieved category, or an empty Optional if not found.
     */
    Optional<Category> getById(Long id);

    /**
     * Updates a category with the specified ID.
     * Обновляет категорию с указанным идентификатором.
     *
     * @param category The updated category object.
     * @param id       The ID of the category to update.
     * @return Optional containing the updated category, or an empty Optional if not found.
     */
    Optional<Category> update(Category category, Long id);

    /**
     * Deletes a category by its ID.
     * Удаляет категорию по ее идентификатору.
     *
     * @param id The ID of the category to delete.
     */
    void deleteById(Long id);

    /**
     * Retrieves all categories associated with the specified user ID.
     * Извлекает все категории, связанные с указанным идентификатором пользователя.
     *
     * @param userId The ID of the user.
     * @return List of categories associated with the user.
     */
    List<Category> getAll(Long userId);


    List<Category> getCategoriesByUserId(Long userId);
}
