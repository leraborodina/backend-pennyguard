package ru.itcolleg.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itcolleg.transaction.model.TransactionLimit;

@Repository
public interface TransactionLimitRepository extends JpaRepository<TransactionLimit, Long> {
    TransactionLimit findByCategoryIdAndUserId(Long categoryId, Long userId);
}