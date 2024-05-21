package ru.itcolleg.transaction.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itcolleg.transaction.model.TransactionType;

import java.util.Optional;

/**
 * Repository for managing TransactionType entities.
 * Репозиторий для управления сущностями TransactionType.
 */
@Repository
public interface TransactionTypeRepository extends CrudRepository<TransactionType, Long> {

    /**
     * Finds a transaction type by type.
     * Находит тип транзакции по типу.
     *
     * @param type The type to search for
     * @return An Optional containing the found transaction type, or empty if not found
     */
    Optional<TransactionType> findByTypeEquals(String type);
}
