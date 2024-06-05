package ru.itcolleg.goal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.itcolleg.goal.dto.FinancialGoalDTO;
import ru.itcolleg.goal.model.FinancialGoal;

/**
 * Маппер для преобразования между DTO объектом и сущностью финансовой цели.
 * Mapper for converting between DTO object and financial goal entity.
 */
@Mapper
public interface FinancialGoalMapper {

    FinancialGoalMapper INSTANCE = Mappers.getMapper(FinancialGoalMapper.class);

    /**
     * Преобразует сущность финансовой цели в DTO объект.
     * Converts a financial goal entity to DTO object.
     *
     * @param entity Сущность финансовой цели
     *               The financial goal entity
     * @return DTO объект финансовой цели
     * The DTO object of financial goal
     */
    FinancialGoalDTO toDTO(FinancialGoal entity);

    /**
     * Преобразует DTO объект финансовой цели в сущность.
     * Converts DTO object of financial goal to entity.
     *
     * @param dto DTO объект финансовой цели
     *            The DTO object of financial goal
     * @return Сущность финансовой цели
     * The financial goal entity
     */
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    FinancialGoal toEntity(FinancialGoalDTO dto);
}
