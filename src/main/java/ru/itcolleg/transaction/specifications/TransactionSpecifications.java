package ru.itcolleg.transaction.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.itcolleg.transaction.model.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class TransactionSpecifications {

    public static Specification<Transaction> hasUserIdEquals(Long userId){
        return (transaction, query, criteriaBuilder) -> criteriaBuilder.equal(transaction.get("userId"), userId);
    }
    public static Specification<Transaction> hasCategoryIdEquals(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return null; // Return null to exclude the condition
            } else {
                return criteriaBuilder.equal(root.get("categoryId"), categoryId);
            }
        };
    }

    public static Specification<Transaction> hasTransactionTypeEquals(Long transactionTypeId){
        return (transaction, query, criteriaBuilder) -> criteriaBuilder.equal(transaction.get("typeId"), transactionTypeId);
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

    public static Specification<Transaction> dateGreaterOrEqual(OffsetDateTime createdAt) {
        return (transaction, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(transaction.get("createdAt"), createdAt);
    }

    public static Specification<Transaction> dateLessOrEqual(OffsetDateTime createdAt) {
        return (transaction, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(transaction.get("createdAt"), createdAt);
    }

}
