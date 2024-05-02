package ru.itcolleg.transaction.service;

import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.dto.TransactionLimitDTO;

import java.util.List;

public interface TransactionLimitService {
    List<TransactionLimitDTO> getTransactionLimitsByUserId(Long userId);
    TransactionLimitDTO getTransactionLimitById(Long id);
    void createTransactionLimit(TransactionLimitDTO limitDTO, Long userId);
    void updateTransactionLimit(TransactionLimitDTO limitDTO);
    void deleteTransactionLimit(Long id);

    void checkLimitsAndSendNotifications(Long userId, List<TransactionDTO> currentExpences);
}


