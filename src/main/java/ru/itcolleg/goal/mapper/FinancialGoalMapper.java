package ru.itcolleg.goal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.itcolleg.goal.dto.FinancialGoalDTO;
import ru.itcolleg.goal.model.FinancialGoal;

@Mapper
public interface FinancialGoalMapper {

    FinancialGoalMapper INSTANCE = Mappers.getMapper(FinancialGoalMapper.class);

    @Mapping(target = "endDate", ignore = true)
    FinancialGoalDTO toDTO(FinancialGoal entity);

    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    FinancialGoal toEntity(FinancialGoalDTO dto);
}
