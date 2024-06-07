package ru.itcolleg.transaction.service;

import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.exception.TransactionNotFoundException;
import ru.itcolleg.transaction.model.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с транзакциями.
 * Service for working with transactions.
 */
public interface TransactionService {

    /**
     * Сохраняет транзакцию для пользователя.
     * Saves a transaction for a user.
     *
     * @param transactionDTO Транзакция для сохранения. The transaction to be saved.
     * @param userId         Идентификатор пользователя. The ID of the user.
     * @return Сохраненная транзакция. The saved transaction.
     */
    Optional<TransactionDTO> saveTransaction(TransactionDTO transactionDTO, Long userId);

    /**
     * Удаляет транзакцию по идентификатору.
     * Deletes a transaction by ID.
     *
     * @param userId Идентификатор пользователя, выполняющего удаление. The ID of the user performing the deletion.
     * @param id     Идентификатор удаляемой транзакции. The ID of the transaction to delete.
     * @throws TransactionNotFoundException     Если транзакция с указанным идентификатором не найдена.
     *                                          If the transaction with the given ID is not found.
     */
    void deleteTransaction(Long userId, Long id) throws TransactionNotFoundException;

    /**
     * Получает транзакцию по идентификатору.
     * Retrieves a transaction by ID.
     *
     * @param id Идентификатор транзакции. The ID of the transaction.
     * @return Транзакция. The transaction.
     * @throws TransactionNotFoundException Если транзакция с указанным идентификатором не найдена.
     *                                      If the transaction with the given ID is not found.
     */
    Optional<TransactionDTO> getTransactionById(Long id) throws TransactionNotFoundException;

    /**
     * Обновляет транзакцию.
     * Updates a transaction.
     *
     * @param transactionDTO The updated transaction DTO.
     * @param id             The ID of the transaction to update.
     * @return The updated transaction DTO.
     * @throws TransactionNotFoundException If the transaction with the given ID is not found.
     */
    Optional<TransactionDTO> updateTransaction(TransactionDTO transactionDTO, Long id, Long userId) throws TransactionNotFoundException;

    /**
     * Получает все транзакции для пользователя.
     * Retrieves all transactions for a user.
     *
     * @param userId The ID of the user.
     * @return Список транзакций. A list of transaction DTOs.
     */
    List<TransactionDTO> getTransactionsByUserId(Long userId);

    List<TransactionDTO> getUserExpenses(Long userId);

    Double calculateUserBalanceAfterSettingGoals(Long userId);

    Double getUserExpensesAfterSetLimits(Long userId, Long categoryId, Integer salaryDay);

    List<TransactionDTO> getFilteredTransactions(Double amount, String purpose, LocalDateTime date, Long categoryId, Long transactionTypeId, Long userId);

    List<TransactionType> getTransactionTypes();
}
