package ru.itcolleg.transaction.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.itcolleg.transaction.model.Transaction;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * Specifications for transactions to be used in JPA queries.
 * Спецификации для транзакций, используемые в запросах JPA.
 */
public class TransactionSpecifications {

    /**
     * Checks if the user ID of the transaction matches the specified value.
     * Проверяет, совпадает ли идентификатор пользователя транзакции с указанным значением.
     *
     * @param userId The user ID.
     * @return The transaction specification.
     * Спецификация транзакции.
     */
    public static Specification<Transaction> hasUserIdEquals(Long userId) {
        return (transaction, query, criteriaBuilder) ->
                criteriaBuilder.equal(transaction.get("userId"), userId);
    }

    /**
     * Checks if the category ID of the transaction matches the specified value.
     * Проверяет, совпадает ли идентификатор категории транзакции с указанным значением.
     *
     * @param categoryId The category ID.
     * @return The transaction specification.
     * Спецификация транзакции.
     */
    public static Specification<Transaction> hasCategoryIdEquals(Long categoryId) {
        return (root, query, criteriaBuilder) ->
                categoryId == null ? null : criteriaBuilder.equal(root.get("categoryId"), categoryId);
    }

    /**
     * Checks if the transaction type matches the specified value.
     * Проверяет, совпадает ли тип транзакции с указанным значением.
     *
     * @param transactionTypeId The transaction type ID.
     * @return The transaction specification.
     * Спецификация транзакции.
     */
    public static Specification<Transaction> hasTransactionTypeEquals(Long transactionTypeId) {
        return (transaction, query, criteriaBuilder) ->
                criteriaBuilder.equal(transaction.get("typeId"), transactionTypeId);
    }

    /**
     * Checks if the transaction amount is greater than or equal to the specified value.
     * Проверяет, является ли сумма транзакции больше или равной указанной.
     *
     * @param amount The amount value.
     * @return The transaction specification.
     * Спецификация транзакции.
     */
    public static Specification<Transaction> amountGreaterOrEqual(double amount) {
        return (transaction, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(transaction.get("amount"), amount);
    }

    /**
     * Checks if the transaction amount is less than or equal to the specified value.
     * Проверяет, является ли сумма транзакции меньше или равной указанной.
     *
     * @param amount The amount value.
     * @return The transaction specification.
     * Спецификация транзакции.
     */
    public static Specification<Transaction> amountLessOrEqual(double amount) {
        return (transaction, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(transaction.get("amount"), amount);
    }

    /**
     * Checks if the purpose of the transaction contains the specified substring.
     * Проверяет, содержит ли цель транзакции указанную подстроку.
     *
     * @param purpose The substring to search for.
     * @return The transaction specification.
     * Спецификация транзакции.
     */
    public static Specification<Transaction> hasPurposeLike(String purpose) {
        return (transaction, query, criteriaBuilder) ->
                criteriaBuilder.like(transaction.get("purpose"), "%" + purpose + "%");
    }

    /**
     * Checks if the creation date of the transaction is greater than or equal to the specified value.
     * Проверяет, является ли дата создания транзакции больше или равной указанной.
     *
     * @param createdAt The creation date for comparison.
     * @return The transaction specification.
     * Спецификация транзакции.
     */
    public static Specification<Transaction> dateGreaterOrEqual(LocalDateTime createdAt) {
        return (transaction, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(transaction.get("createdAt"), createdAt);
    }

    /**
     * Checks if the creation date of the transaction is less than or equal to the specified value.
     * Проверяет, является ли дата создания транзакции меньше или равной указанной.
     *
     * @param createdAt The creation date for comparison.
     * @return The transaction specification.
     * Спецификация транзакции.
     */
    public static Specification<Transaction> dateLessOrEqual(LocalDateTime createdAt) {
        return (transaction, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(transaction.get("createdAt"), createdAt);
    }
}
