package ru.itcolleg.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itcolleg.transaction.model.Transaction;

import java.util.List;

/**
 * Repository for managing Transaction entities.
 * Репозиторий для управления сущностями Transaction.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    /**
     * Finds transactions by user ID.
     * Находит транзакции по идентификатору пользователя.
     *
     * @param userId The ID of the user
     * @return List of transactions
     */
    List<Transaction> findTransactionsByUserId(Long userId);

    /**
     * Finds transactions by user ID, type ID, and regular status.
     * Находит транзакции по идентификатору пользователя, идентификатору типа и статусу регулярности.
     *
     * @param userId     The ID of the user
     * @param typeId     The ID of the type
     * @param notRegular The regular status
     * @return List of transactions
     */
    List<Transaction> findTransactionsByUserIdAndTypeIdAndRegular(Long userId, Long typeId, Long notRegular);

    List<Transaction> findByRegularTrue();
}
