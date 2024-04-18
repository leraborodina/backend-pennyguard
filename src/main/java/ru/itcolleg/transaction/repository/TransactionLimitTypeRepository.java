package ru.itcolleg.transaction.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itcolleg.transaction.model.TransactionLimitType;

@Repository
public interface TransactionLimitTypeRepository extends CrudRepository<TransactionLimitType, Long> {
}
