package ru.itcolleg.transaction.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itcolleg.transaction.model.TransactionType;

import java.util.Optional;

@Repository
public interface TransactionTypeRepository extends CrudRepository<TransactionType, Long> {
    Optional<TransactionType> findByTypeEquals(String type);
}
