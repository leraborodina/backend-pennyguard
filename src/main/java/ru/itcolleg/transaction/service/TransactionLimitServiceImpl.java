package ru.itcolleg.transaction.service;

import org.springframework.stereotype.Service;
import ru.itcolleg.transaction.dto.TransactionLimitDTO;
import ru.itcolleg.transaction.mapper.TransactionLimitMapper;
import ru.itcolleg.transaction.model.TransactionLimit;
import ru.itcolleg.transaction.model.TransactionLimitType;
import ru.itcolleg.transaction.repository.TransactionLimitRepository;
import ru.itcolleg.transaction.repository.TransactionLimitTypeRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TransactionLimitServiceImpl implements TransactionLimitService {

    private final TransactionLimitRepository transactionLimitRepository;
    private final TransactionLimitTypeRepository transactionLimitTypeRepository;
    private final TransactionLimitMapper transactionLimitMapper;

    public TransactionLimitServiceImpl(TransactionLimitRepository transactionLimitRepository, TransactionLimitTypeRepository transactionLimitTypeRepository, TransactionLimitMapper transactionLimitMapper) {
        this.transactionLimitRepository = transactionLimitRepository;
        this.transactionLimitTypeRepository = transactionLimitTypeRepository;
        this.transactionLimitMapper = transactionLimitMapper;
    }

    @Override
    public TransactionLimitDTO getTransactionLimitById(Long id) {
        validateId(id);
        Optional<TransactionLimit> limitOptional = transactionLimitRepository.findById(id);
        return limitOptional.map(transactionLimitMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Transaction limit not found with id: " + id));
    }

    @Override
    public void setTransactionLimit(TransactionLimitDTO limitDTO, Long userId) {
        validateLimitDTO(limitDTO);
        try {
            TransactionLimit limit = transactionLimitMapper.toEntity(limitDTO);
            limit.setUserId(userId);
            transactionLimitRepository.save(limit);
        } catch (Exception e) {
            handleException("Failed to set transaction limit", e);
        }
    }

    @Override
    public void updateTransactionLimit(TransactionLimitDTO limitDTO) {
        validateLimitDTO(limitDTO);
        try {
            TransactionLimit limit = transactionLimitMapper.toEntity(limitDTO);
            transactionLimitRepository.save(limit);
        } catch (Exception e) {
            handleException("Failed to update transaction limit", e);
        }
    }

    @Override
    public List<TransactionLimitDTO> getAllTransactionLimits() {
        return transactionLimitRepository.findAll().stream()
                .map(transactionLimitMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTransactionLimit(Long id) {
        validateId(id);
        try {
            transactionLimitRepository.deleteById(id);
        } catch (Exception e) {
            handleException("Failed to delete transaction limit", e);
        }
    }

    @Override
    public List<TransactionLimitType> getLimitTypes() {
        Iterable<TransactionLimitType> limitTypesIterable = transactionLimitTypeRepository.findAll();
        return iterableToList(limitTypesIterable);
    }

    private <T> List<T> iterableToList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
    }

    private void validateLimitDTO(TransactionLimitDTO limitDTO) {
        if (limitDTO == null) {
            throw new IllegalArgumentException("Transaction limit DTO cannot be null");
        }
    }

    private void handleException(String message, Exception e) {
        throw new RuntimeException(message + ": " + e.getMessage(), e);
    }
}
