package ru.itcolleg.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.exception.TransactionNotFoundException;
import ru.itcolleg.transaction.exception.UnauthorizedTransactionException;
import ru.itcolleg.transaction.mapper.TransactionMapper;
import ru.itcolleg.transaction.model.Category;
import ru.itcolleg.transaction.model.Transaction;
import ru.itcolleg.transaction.model.TransactionType;
import ru.itcolleg.transaction.repository.CategoryRepository;
import ru.itcolleg.transaction.repository.TransactionRepository;
import ru.itcolleg.transaction.repository.TransactionTypeRepository;
import ru.itcolleg.transaction.specifications.TransactionSpecifications;

import java.time.LocalDate;
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
    public Optional<TransactionDTO> saveTransaction(TransactionDTO transactionDTO, Long userId) {

        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setCategoryId(transactionDTO.getCategoryId());
        transaction.setTransactionTypeId(transactionDTO.getTransactionTypeId());
        transaction.setDate(transactionDTO.getDate());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setPurpose(transactionDTO.getPurpose());
        transaction.setRegular(transactionDTO.getRegular());

        Transaction dbTransaction = transactionRepository.save(transaction);

        TransactionDTO savedTransaction = TransactionMapper.mapTransactionToTransactionDTO(dbTransaction);
        return Optional.ofNullable(savedTransaction);
    }

    @Override
    public void deleteTransaction(Long extractedUserId, Long id) throws TransactionNotFoundException, UnauthorizedTransactionException {
        // Find the transaction by its ID in the repository
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);

        // Check if the transaction exists
        if (transactionOptional.isPresent()) {
            // If the transaction exists, retrieve it
            Transaction transaction = transactionOptional.get();

            // Check if the extracted user ID matches the user ID associated with the transaction
            if (transaction.getUserId().equals(extractedUserId)) {
                // If the user IDs match, delete the transaction from the repository
                transactionRepository.deleteById(id);
            } else {
                // If the user IDs don't match, throw an UnauthorizedTransactionException
                throw new UnauthorizedTransactionException("UserIds don't match");
            }
        } else {
            // If the transaction doesn't exist, throw a TransactionNotFoundException
            throw new TransactionNotFoundException("Transaction not found for id " + id);
        }
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
        transaction.setTransactionTypeId(transactionDTO.getTransactionTypeId());

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
    public List<TransactionDTO> getAll(Double amount, String purpose, LocalDate date, Long categoryId, Long transactionTypeId, Long userId) {

        if (userId == null) {
            return Collections.emptyList();
        }

        Specification<Transaction> searchQuery = Specification.where(TransactionSpecifications.hasUserIdEquals(userId));
        // select * from transaction where userId = {userId}

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
            searchQuery = searchQuery.and(TransactionSpecifications.dateEqual(date));
        }

        if (categoryId != null) {
            searchQuery = searchQuery.or(TransactionSpecifications.hasCategoryEquals(categoryId));
        }

        if(transactionTypeId != null){
            searchQuery = searchQuery.or(TransactionSpecifications.hasTransactionTypeEquals(transactionTypeId));
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