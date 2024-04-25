package ru.itcolleg.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itcolleg.transaction.model.Category;

import java.util.List;
import java.util.Optional;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameContainingIgnoreCase(String value);

    // Find categories where isDefault is true
    List<Category> findByIsDefaultTrue();

    List<Category> findByIsDefaultTrueOrIsDefaultIsNullAndUserId(Long userId);
}
