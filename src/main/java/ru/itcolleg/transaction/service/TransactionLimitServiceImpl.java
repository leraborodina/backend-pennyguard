package ru.itcolleg.transaction.service;

import org.springframework.stereotype.Service;
import ru.itcolleg.transaction.dto.TransactionLimitDTO;
import ru.itcolleg.transaction.model.TransactionLimit;
import ru.itcolleg.transaction.model.TransactionLimitType;
import ru.itcolleg.transaction.repository.TransactionLimitRepository;
import ru.itcolleg.transaction.repository.TransactionLimitTypeRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionLimitServiceImpl implements TransactionLimitService {

    private final TransactionLimitRepository transactionLimitRepository;
    private final TransactionLimitTypeRepository transactionLimitTypeRepository;

    public TransactionLimitServiceImpl(TransactionLimitRepository transactionLimitRepository, TransactionLimitTypeRepository transactionLimitTypeRepository) {
        this.transactionLimitRepository = transactionLimitRepository;
        this.transactionLimitTypeRepository = transactionLimitTypeRepository;
    }

    @Override
    public TransactionLimitDTO getTransactionLimitById(Long id) {
        TransactionLimit limit = transactionLimitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction limit not found with id: " + id));
        return convertToDTO(limit);
    }

    @Override
    public void setTransactionLimit(TransactionLimitDTO limitDTO) {
        TransactionLimit limit = convertToEntity(limitDTO);
        transactionLimitRepository.save(limit);
    }

    @Override
    public void updateTransactionLimit(TransactionLimitDTO limitDTO) {
        TransactionLimit limit = convertToEntity(limitDTO);
        transactionLimitRepository.save(limit);
    }

    @Override
    public List<TransactionLimitDTO> getAllTransactionLimits() {
        return transactionLimitRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTransactionLimit(Long id) {
        transactionLimitRepository.deleteById(id);
    }

    private TransactionLimitDTO convertToDTO(TransactionLimit limit) {
        TransactionLimitDTO dto = new TransactionLimitDTO();
        dto.setId(limit.getId());
        dto.setUserId(limit.getUserId());
        dto.setCategoryId(limit.getCategoryId());
        dto.setLimitType(limit.getLimitType().getType());
        dto.setLimitValue(limit.getLimitValue());
        return dto;
    }

    private TransactionLimit convertToEntity(TransactionLimitDTO dto) {
        TransactionLimit limit = new TransactionLimit();
        limit.setId(dto.getId());
        limit.setUserId(dto.getUserId());
        limit.setCategoryId(dto.getCategoryId());
        Optional<TransactionLimitType> limitType = transactionLimitTypeRepository.findLimitTypeByType(dto.getLimitType());
        limit.setLimitType(limitType.get());
        limit.setLimitValue(dto.getLimitValue());
        return limit;
    }
}
