package ru.itcolleg.transaction.repository;

import org.springframework.data.repository.CrudRepository;
import ru.itcolleg.transaction.model.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
