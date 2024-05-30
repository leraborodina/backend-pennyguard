package ru.itcolleg.transaction.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.model.Transaction;

/**
 * Mapper interface for mapping Transaction entities to DTOs and vice versa.
 * Интерфейс маппера для отображения сущностей Transaction на DTO и обратно.
 */
@Mapper
public interface TransactionMapper {

    /**
     * Gets the instance of the TransactionMapper.
     * Возвращает экземпляр TransactionMapper.
     */
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    /**
     * Maps a Transaction entity to a TransactionDTO.
     * Сопоставляет DTO TransactionDTO с сущностью Transaction.
     *
     * @param transaction The Transaction entity
     * @return The corresponding TransactionDTO
     */
    TransactionDTO toTransactionDTO(Transaction transaction);

    /**
     * Maps a TransactionDTO to a Transaction entity.
     * Сопоставляет сущность Transaction с TransactionDTO.
     *
     * @param transactionDTO The TransactionDTO
     * @return The corresponding Transaction entity
     */
    Transaction toTransaction(TransactionDTO transactionDTO);
}
