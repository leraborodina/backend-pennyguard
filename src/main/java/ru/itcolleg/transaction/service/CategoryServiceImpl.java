package ru.itcolleg.transaction.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itcolleg.transaction.exception.CategoryNotFoundException;
import ru.itcolleg.transaction.model.Category;
import ru.itcolleg.transaction.repository.CategoryRepository;

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
    public Optional<Category> save(Category category) {
        logger.info("Сохраняет категорию.");
        if (category == null) {
            throw new IllegalArgumentException("Категория не может быть пустой");
        }
        return Optional.of(categoryRepository.save(category));
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
        existingCategory.setName(category.getName());

        return Optional.of(categoryRepository.save(existingCategory));
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

        List<Category> categories;
        if (userId != null) {
            categories = categoryRepository.findByIsDefaultTrueOrUserId(userId);
        } else {
            categories = categoryRepository.findByIsDefaultTrue();
        }

        if (categories.isEmpty()) {
            throw new DataAccessException("Категории не найдены") {};
        }

        return categories;
    }
}
