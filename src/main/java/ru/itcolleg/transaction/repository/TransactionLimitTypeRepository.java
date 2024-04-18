package ru.itcolleg.transaction.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itcolleg.transaction.model.TransactionLimitType;

import java.util.Optional;

@Repository
public interface TransactionLimitTypeRepository extends CrudRepository<TransactionLimitType, Long> {
    Optional<TransactionLimitType> findLimitTypeByType(String type);
}
