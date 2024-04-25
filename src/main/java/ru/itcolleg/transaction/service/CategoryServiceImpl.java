package ru.itcolleg.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itcolleg.transaction.model.Category;
import ru.itcolleg.transaction.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Category> save(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        return Optional.of(categoryRepository.save(category));
    }

    @Override
    public Optional<Category> getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<Category> update(Category category, Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        Optional<Category> existingCategoryOptional = categoryRepository.findById(category.getId());
        if (existingCategoryOptional.isEmpty()) {
            return Optional.empty();
        }

        Category existingCategory = existingCategoryOptional.get();
        existingCategory.setName(category.getName());

        return Optional.of(categoryRepository.save(existingCategory));
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAll(Long userId) {
        // Find categories where isDefault is true or (isDefault is null or false) and userId = :userId if userId is not null
        List<Category> categories;
        if (userId != null) {
            categories = categoryRepository.findByIsDefaultTrueOrIsDefaultIsNullAndUserId(userId);
        } else {
            categories = categoryRepository.findByIsDefaultTrue();
        }

        if (categories.isEmpty()) {
            throw new DataAccessException("No categories found") {};
        }

        return categories;
    }
}
