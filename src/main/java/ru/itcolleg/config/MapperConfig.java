package ru.itcolleg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.itcolleg.transaction.mapper.TransactionLimitMapper;
import ru.itcolleg.transaction.mapper.TransactionMapper;

@Configuration
public class MapperConfig {

    @Bean
    public TransactionMapper transactionMapper() {
        return TransactionMapper.INSTANCE;
    }

    @Bean
    public TransactionLimitMapper transactionLimitMapper() {
        return TransactionLimitMapper.INSTANCE;
    }
}
