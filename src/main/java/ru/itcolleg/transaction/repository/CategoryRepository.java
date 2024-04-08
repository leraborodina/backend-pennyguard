package ru.itcolleg.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itcolleg.transaction.model.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByValueContainingIgnoreCase(String value);
}
