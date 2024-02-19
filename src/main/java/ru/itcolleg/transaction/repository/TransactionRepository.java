package ru.itcolleg.transaction.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import ru.itcolleg.transaction.model.Transaction;

import javax.persistence.Id;
import java.util.Optional;

public interface TransactionRepository extends CrudRepository <Transaction, Long>, JpaSpecificationExecutor<Transaction> {

}
