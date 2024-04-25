package ru.itcolleg.transaction.service;

import ru.itcolleg.transaction.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<Category> save(Category category);

    Optional<Category> getById(Long id);

    Optional<Category> update(Category category, Long id);

    void deleteById(Long id);

    List<Category> getAll(Long userId);
}
