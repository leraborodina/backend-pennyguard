package ru.itcolleg.transaction.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itcolleg.transaction.exception.CategoryNotFoundException;
import ru.itcolleg.transaction.model.Category;
import ru.itcolleg.transaction.repository.CategoryRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing categories.
 * Реализация сервиса для управления категориями.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    @Transactional
    public Optional<Category> save(Category category, Long userId) {
        logger.info("Сохраняет категорию.");

        if (category == null) {
            throw new IllegalArgumentException("Категория не может быть пустой");
        }

        if (userId == null) {
            throw new IllegalArgumentException("ID пользователя не может быть пустой");
        }

        // Check if the category name already exists for the user
        boolean categoryExists = categoryRepository.existsByNameAndUserId(category.getName(), userId);

        // Check if the category name is a default category
        boolean isDefaultCategory = categoryRepository.existsByNameAndUserIdAndIsDefault(category.getName(), null, true);

        if (categoryExists || isDefaultCategory) {
            throw new DuplicateKeyException("Категория уже существует или является стандартной");
        }

        try {
            category.setUserId(userId);
            return Optional.of(categoryRepository.save(category));
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка при сохранении категории: {}", e.getMessage());
            throw e; // Re-throw the exception to be handled by the controller
        } catch (DataIntegrityViolationException e) {
            logger.error("Ошибка при сохранении категории: {}", e.getMessage());
            throw new DuplicateKeyException("Категория уже существует", e);
        } catch (Exception e) {
            logger.error("Ошибка при сохранении категории: {}", e.getMessage());
            throw new RuntimeException("Ошибка при сохранении категории", e);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Category> getById(Long id) {
        logger.info("Получает категорию по ее идентификатору.");
        if (id == null) {
            throw new IllegalArgumentException("ID не может быть пустым");
        }
        return categoryRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Category> update(Category category, Long id) {
        logger.info("Обновляет категорию с указанным идентификатором.");

        if (id == null) {
            throw new IllegalArgumentException("ID не может быть пустым");
        }

        Optional<Category> existingCategoryOptional = categoryRepository.findById(id);

        if (existingCategoryOptional.isEmpty()) {
            throw new CategoryNotFoundException("Категория с идентификатором " + id + " не найдена");
        }

        Category existingCategory = existingCategoryOptional.get();

        boolean isDefaultCategory = categoryRepository.existsByNameAndUserIdAndIsDefault(category.getName(), null, true);
        if (isDefaultCategory) {
            throw new IllegalArgumentException("Нельзя обновить категорию до имени, которое является стандартной категорией");
        }

        existingCategory.setName(category.getName());


        try {
            return Optional.of(categoryRepository.save(existingCategory));
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка при обновлении категории: {}", e.getMessage());
            throw e; // Re-throw the exception to be handled by the controller
        } catch (DataIntegrityViolationException e) {
            logger.error("Ошибка при обновлении категории: {}", e.getMessage());
            throw new DuplicateKeyException("Категория уже существует", e);
        } catch (Exception e) {
            logger.error("Ошибка при обновлении категории: {}", e.getMessage());
            throw new RuntimeException("Ошибка при обновлении категории", e);
        }
    }


    @Override
    @Transactional
    public void deleteById(Long id) {
        logger.info("Удаляет категорию по ее идентификатору.");
        if (id == null) {
            throw new IllegalArgumentException("ID не может быть пустым");
        }
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException("Категория с идентификатором " + id + " не найдена");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAll(Long userId) {
        logger.info("Получает все категории.");

        List<Category> categories = categoryRepository.findByIsDefaultTrue();

        if (userId != null) {
            categories.addAll(categoryRepository.findByIsDefaultFalseAndUserId(userId));
        }

        if (categories.isEmpty()) {
            throw new DataAccessException("Категории не найдены") {
            };
        }

        return categories;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getCategoriesByUserId(Long userId) {
        logger.info("Получает все категории.");

        List<Category> userCategories = categoryRepository.findByIsDefaultFalseAndUserId(userId);
        return userCategories.isEmpty() ? Collections.emptyList() : userCategories;
    }
}
