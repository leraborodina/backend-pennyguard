package ru.itcolleg.transaction.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.model.Transaction;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Mapper
public interface TransactionMapper {

    String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "createdAt", dateFormat = DATE_TIME_FORMAT)
    TransactionDTO toTransactionDTO(Transaction transaction);

    Transaction toTransaction(TransactionDTO transactionDTO);

    default OffsetDateTime parseCreatedAt(String createdAt) {
        return OffsetDateTime.parse(createdAt, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }
}
