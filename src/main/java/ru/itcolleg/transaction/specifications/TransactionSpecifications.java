package ru.itcolleg.transaction.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.itcolleg.transaction.model.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionSpecifications {

    public static Specification<Transaction> hasUserIdEquals(Long userId){
        return (transaction, query, criteriaBuilder) -> criteriaBuilder.equal(transaction.get("userId"), userId);
    }
    public static Specification<Transaction> hasCategoryEquals(Long categoryId){
        return (transaction, query, criteriaBuilder) -> criteriaBuilder.equal(transaction.get("categoryId"), categoryId);
    }

    public static Specification<Transaction> hasTransactionTypeEquals(Long transactionTypeId){
        return (transaction, query, criteriaBuilder) -> criteriaBuilder.equal(transaction.get("transactionTypeId"), transactionTypeId);
    }

    public static Specification<Transaction> amountGreaterOrEqual(double amount){
        return (transaction, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(transaction.get("amount"), amount);
    }

    public static Specification<Transaction> amountLessOrEqual(double amount){
        return (transaction, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(transaction.get("amount"), amount);
    }

    public static Specification<Transaction> hasPurposeLike(String purpose){
        return (transaction, query, criteriaBuilder) -> criteriaBuilder.like(transaction.get("purpose"), "%" + purpose + "%");
    }

    public static Specification<Transaction> dateEqual(LocalDate dateTime){
        return (transaction, query, criteriaBuilder) -> criteriaBuilder.equal(transaction.get("date"),dateTime);
    }

    public static Specification<Transaction> dateLessOrEqual(LocalDate dateTime){
        return (transaction, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(transaction.get("date"),dateTime);
    }

    // TODO 3: create function for searching after userId in the database table transaction
}
