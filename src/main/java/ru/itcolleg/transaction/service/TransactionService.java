package ru.itcolleg.transaction.service;

import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.exception.TransactionNotFoundException;
import ru.itcolleg.transaction.model.Category;
import ru.itcolleg.transaction.model.TransactionType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionService {
    Optional<TransactionDTO> saveTransaction(TransactionDTO transactionDTO);

    Optional<TransactionDTO> getTransactionById(Long id) throws TransactionNotFoundException;

    Optional<TransactionDTO> updateTransaction(TransactionDTO transactionDTO, Long id) throws TransactionNotFoundException;

    List<TransactionDTO> getAll(Double amount, String purpose, LocalDate date, String category);

    List<Category> getCategories();
    List<TransactionType> getTransactionTypes();
    List<TransactionDTO> getTransactionsByUserId(Long userId);
}
