package ru.itcolleg.transaction.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.itcolleg.transaction.dto.TransactionLimitDTO;
import ru.itcolleg.transaction.model.TransactionLimit;

@Mapper
public interface TransactionLimitMapper {

    TransactionLimitMapper INSTANCE = Mappers.getMapper(TransactionLimitMapper.class);

    TransactionLimitDTO toDto(TransactionLimit limit);

    TransactionLimit toEntity(TransactionLimitDTO dto);
}

