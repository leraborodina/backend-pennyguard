package ru.itcolleg.transaction.repository;

import org.springframework.data.repository.CrudRepository;
import ru.itcolleg.transaction.model.TransactionType;

public interface TransactionTypeRepository extends CrudRepository<TransactionType, Long> {
}
