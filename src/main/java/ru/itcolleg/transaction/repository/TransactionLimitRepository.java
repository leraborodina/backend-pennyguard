package ru.itcolleg.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itcolleg.transaction.model.TransactionLimit;

import java.util.List;

@Repository
public interface TransactionLimitRepository extends JpaRepository<TransactionLimit, Long> {
    List<TransactionLimit> findByUserId(Long userId);
}