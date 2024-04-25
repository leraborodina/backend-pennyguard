package ru.itcolleg.transaction.mapper;


import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.model.Transaction;
import java.time.format.DateTimeFormatter;
import java.time.OffsetDateTime;

import org.mapstruct.Mapper;

@Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "createdAtStr", expression = "java(formatCreatedAt(transaction.getCreatedAt()))")
    TransactionDTO toTransactionDTO(Transaction transaction);

    @Mapping(target = "createdAt", expression = "java(parseCreatedAt(transactionDTO.getCreatedAtStr()))")
    Transaction toTransaction(TransactionDTO transactionDTO);

    default String formatCreatedAt(OffsetDateTime createdAt) {
        return createdAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    default OffsetDateTime parseCreatedAt(String createdAtStr) {
        return OffsetDateTime.parse(createdAtStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
