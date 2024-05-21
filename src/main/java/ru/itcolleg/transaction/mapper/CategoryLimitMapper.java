package ru.itcolleg.transaction.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.itcolleg.transaction.dto.CategoryLimitDTO;
import ru.itcolleg.transaction.model.CategoryLimit;

/**
 * Mapper interface for mapping CategoryLimit entities to DTOs and vice versa.
 * Интерфейс маппера для отображения сущностей CategoryLimit на DTO и обратно.
 */
@Mapper
public interface CategoryLimitMapper {

    /**
     * Gets the instance of the CategoryLimitMapper.
     * Возвращает экземпляр CategoryLimitMapper.
     */
    CategoryLimitMapper INSTANCE = Mappers.getMapper(CategoryLimitMapper.class);

    /**
     * Maps a CategoryLimit entity to a CategoryLimitDTO.
     * Сопоставляет сущность CategoryLimit с DTO CategoryLimitDTO.
     *
     * @param limit CategoryLimit entity
     * @return CategoryLimitDTO DTO
     */
    CategoryLimitDTO toDto(CategoryLimit limit);

    /**
     * Maps a CategoryLimitDTO to a CategoryLimit entity.
     * Сопоставляет DTO CategoryLimitDTO с сущностью CategoryLimit.
     *
     * @param dto CategoryLimitDTO DTO
     * @return CategoryLimit entity
     */
    CategoryLimit toEntity(CategoryLimitDTO dto);
}
