package ru.itcolleg.transaction.service;

import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.exception.TransactionNotFoundException;
import ru.itcolleg.transaction.exception.UnauthorizedTransactionException;
import ru.itcolleg.transaction.model.TransactionType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionService {
    Optional<TransactionDTO> saveTransaction(TransactionDTO transactionDTO, Long userId);

    void deleteTransaction(Long extractedUserId, Long id) throws TransactionNotFoundException, UnauthorizedTransactionException;

    Optional<TransactionDTO> getTransactionById(Long id) throws TransactionNotFoundException;

    Optional<TransactionDTO> updateTransaction(TransactionDTO transactionDTO, Long id) throws TransactionNotFoundException;

    List<TransactionDTO> getAll(Double amount, String purpose, LocalDate date, Long categoryId, Long transactionTypeId, Long userId);

    List<TransactionType> getTransactionTypes();

    List<TransactionDTO> getTransactionsByUserId(Long userId);

    Double getUserFixExpenses(Long userId, Long categoryId, Integer salaryDay);

    List<TransactionDTO> getUserExpencesByPeriodAndCategory(Long userId, Long typeId);

    Double calculateUserBalanceAfterSettingGoals(Long userId);
}
