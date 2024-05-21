package ru.itcolleg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.itcolleg.goal.mapper.FinancialGoalMapper;
import ru.itcolleg.transaction.mapper.CategoryLimitMapper;
import ru.itcolleg.transaction.mapper.TransactionMapper;
import ru.itcolleg.user.mapper.UserMapper;

/**
 * Configuration class for defining mapper beans.
 * This class is responsible for configuring and providing instances of mapper beans used in the application.
 * Each method within this class defines a bean for a specific mapper, ensuring that these mapper instances are available
 * throughout the application where they are needed.
 * <p>
 * Класс конфигурации для определения бинов мапперов.
 * Этот класс отвечает за настройку и предоставление экземпляров бинов мапперов, используемых в приложении.
 * Каждый метод в этом классе определяет бин для определенного маппера, гарантируя, что эти экземпляры доступны
 * во всем приложении, где они нужны.
 */

@Configuration
public class MapperConfig {

    /**
     * Provides a TransactionMapper bean.
     * Предоставляет бин TransactionMapper.
     *
     * @return TransactionMapper bean instance.
     * Экземпляр бина TransactionMapper.
     */
    @Bean
    public TransactionMapper transactionMapper() {
        return TransactionMapper.INSTANCE;
    }

    /**
     * Provides a CategoryLimitMapper bean.
     * Предоставляет бин CategoryLimitMapper.
     *
     * @return CategoryLimitMapper bean instance.
     * Экземпляр бина CategoryLimitMapper.
     */
    @Bean
    public CategoryLimitMapper transactionLimitMapper() {
        return CategoryLimitMapper.INSTANCE;
    }

    /**
     * Provides a FinancialGoalMapper bean.
     * Предоставляет бин FinancialGoalMapper.
     *
     * @return FinancialGoalMapper bean instance.
     * Экземпляр бина FinancialGoalMapper.
     */
    @Bean
    public FinancialGoalMapper financialGoalMapper() {
        return FinancialGoalMapper.INSTANCE;
    }

    /**
     * Provides a UserMapper bean.
     * Предоставляет бин UserMapper.
     *
     * @return UserMapper bean instance.
     * Экземпляр бина UserMapper.
     */
    @Bean
    public UserMapper userMapper() {
        return UserMapper.INSTANCE;
    }
}
