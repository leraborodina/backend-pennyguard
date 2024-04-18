package ru.itcolleg.transaction.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itcolleg.transaction.model.TransactionLimitType;
import ru.itcolleg.transaction.model.TransactionType;
@Repository
public interface TransactionTypeRepository extends CrudRepository<TransactionType, Long> {
}
