package ru.itcolleg.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
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
        existingCategory.setValue(category.getValue());

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
    public List<Category> getAll() {
        List<Category> allCategories = categoryRepository.findAll();
        if (allCategories.isEmpty()) {
            throw new DataAccessException("No categories found") {};
        }
        return allCategories;
    }
}
