package ru.itcolleg.transaction.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itcolleg.transaction.model.Transaction;

import javax.persistence.Id;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends CrudRepository <Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    List<Transaction> findTransactionsByUserId(Long userId);

    List<Transaction> findTransactionsByUserIdAndTypeIdAndRegular(Long userId, Long typeId, Long notRegular);
}
