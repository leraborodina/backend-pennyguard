package ru.itcolleg.transaction.mapper;

import org.modelmapper.ModelMapper;
import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.model.Transaction;

public class TransactionMapper {
    private static final ModelMapper modelMapper= new ModelMapper();

    public static TransactionDTO mapTransactionToTransactionDTO(Transaction transaction){
        return modelMapper.map(transaction, TransactionDTO.class);
    }
}
