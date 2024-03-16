package ru.itcolleg.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.exception.TransactionNotFoundException;
import ru.itcolleg.transaction.mapper.TransactionMapper;
import ru.itcolleg.transaction.model.Category;
import ru.itcolleg.transaction.model.Transaction;
import ru.itcolleg.transaction.model.TransactionType;
import ru.itcolleg.transaction.repository.CategoryRepository;
import ru.itcolleg.transaction.repository.TransactionRepository;
import ru.itcolleg.transaction.repository.TransactionTypeRepository;
import ru.itcolleg.transaction.specifications.TransactionSpecifications;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionTypeRepository transactionTypeRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, CategoryRepository categoryRepository, TransactionTypeRepository transactionTypeRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.transactionTypeRepository = transactionTypeRepository;
    }

    @Override
    public Optional<TransactionDTO> saveTransaction(TransactionDTO transactionDTO) {

        Transaction transaction = new Transaction();

        transaction.setCategoryId(transactionDTO.getCategoryId());
        transaction.setTransactionTypeId(transactionDTO.getTransactionTypeId());
        transaction.setUserId(transactionDTO.getUserId());
        transaction.setDate(transactionDTO.getDate());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setPurpose(transactionDTO.getPurpose());
        transaction.setRegular(transactionDTO.getRegular());

        Transaction dbTransaction = transactionRepository.save(transaction);

        TransactionDTO savedTransaction = TransactionMapper.mapTransactionToTransactionDTO(dbTransaction);
        return Optional.ofNullable(savedTransaction);
    }

    @Override
    public Optional<TransactionDTO> getTransactionById(Long id) throws TransactionNotFoundException {
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);
        if (transactionOptional.isPresent()) {
            TransactionDTO savedTransaction = TransactionMapper.mapTransactionToTransactionDTO(transactionOptional.get());
            return Optional.ofNullable(savedTransaction);
        } else {
            throw new TransactionNotFoundException("Transaction not found for id" + id);
        }
    }

    @Override
    public Optional<TransactionDTO> updateTransaction(TransactionDTO transactionDTO, Long id) throws TransactionNotFoundException {
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);

        Transaction transaction = transactionOptional.get();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setRegular(transactionDTO.getRegular());
        transaction.setDate(transactionDTO.getDate());
        transaction.setPurpose(transactionDTO.getPurpose());
        transaction.setCategoryId(transactionDTO.getCategoryId());

        Transaction savedTransaction = transactionRepository.save(transaction);
        //  TODO: создать package category(model,repository,dto?)
        //  TODO: проверить по id, существует ли обновленная категория
        /*
         * Optional <Category> category = categoryRepository.findById(transactionDTO.getCategoryId());
         *
         * if(category.isPresent()){
         *   transaction.setCategoryId(category.get().getId())
         * }
         * */

        TransactionDTO dto = TransactionMapper.mapTransactionToTransactionDTO(savedTransaction);
        return Optional.ofNullable(dto);
    }

    @Override
    public List<TransactionDTO> getTransactionsByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }

        List<Transaction> foundTransactions = transactionRepository.findTransactionsByUserId(userId);

        return foundTransactions.stream()
                .map(TransactionMapper::mapTransactionToTransactionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> getAll(Double amount, String purpose, LocalDate date, String category) {
        // TODO 1: add userId to parameters -> i cant search after transactions without userId.

        // TODO 2: if userId == null than return empty list

        Specification<Transaction> searchQuery = Specification.where(null);
        // select * from transaction

        // TODO 4: Add userId to specifications

        if (amount != null && amount > 0) {
            searchQuery = searchQuery.and(TransactionSpecifications.amountGreaterOrEqual(amount));
            // select * from transaction where amount > {amount}
        }

        if (purpose != null && !purpose.isEmpty()) {
            searchQuery = searchQuery.and(TransactionSpecifications.hasPurposeLike(purpose));
            // select * from transaction where amount > {amount} and purpose like '%{purpose}%'
        }

        // TODO 5: check data sql (Example: 16.03.2024 -> searching for 16 and 17 march, not correct)
        if (date != null) {
            searchQuery = searchQuery.and(TransactionSpecifications.dateGreaterOrEqual(date));
        }

        // TODO 6: you are getting a string value from frontend. Firstly check if its true. Send category name from frontend and not categoryId.
        if (category != null && !category.isEmpty()) {
            searchQuery = searchQuery.or(TransactionSpecifications.hasCategoryEquals(category));
        }

        List<Transaction> foundTransactions = transactionRepository.findAll(searchQuery);

        return foundTransactions.stream()
                .map(TransactionMapper::mapTransactionToTransactionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> getCategories() {
        Iterable<Category> categories = categoryRepository.findAll();
        return iterableToList(categories);
    }

    @Override
    public List<TransactionType> getTransactionTypes() {
        Iterable<TransactionType> transactionTypes = transactionTypeRepository.findAll();
        return iterableToList(transactionTypes);
    }

    private <T> List<T> iterableToList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }
}