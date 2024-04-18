package ru.itcolleg.transaction.service;

import ru.itcolleg.transaction.dto.TransactionLimitDTO;

import java.util.List;

public interface TransactionLimitService {
    List<TransactionLimitDTO> getAllTransactionLimits();
    TransactionLimitDTO getTransactionLimitById(Long id);
    void setTransactionLimit(TransactionLimitDTO limitDTO, Long userId);
    void updateTransactionLimit(TransactionLimitDTO limitDTO);
    void deleteTransactionLimit(Long id);
}


