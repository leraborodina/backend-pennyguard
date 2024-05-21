package ru.itcolleg.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.itcolleg.auth.dto.LoginResponse;
import ru.itcolleg.user.dto.UserDTO;
import ru.itcolleg.user.model.User;

/**
 * Mapper interface responsible for mapping User entities to LoginResponse DTOs.
 * Интерфейс маппера, ответственный за преобразование сущностей пользователя в DTO-объекты LoginResponse.
 */
@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Maps a User entity to a LoginResponse DTO.
     * Преобразует сущность пользователя в DTO-объект LoginResponse.
     *
     * @param user the User entity to be mapped.
     *             сущность пользователя для преобразования.
     * @return the corresponding LoginResponse DTO.
     * соответствующий объект DTO LoginResponse.
     */
    LoginResponse mapUserToLoginResponse(User user);

    /**
     * Maps a UserDTO to a User entity.
     * Преобразует объект UserDTO в сущность пользователя.
     *
     * @param userDTO the UserDTO to be mapped.
     *                объект UserDTO для преобразования.
     * @return the corresponding User entity.
     * соответствующая сущность пользователя.
     */
    @Mapping(target = "password", ignore = true)
    User mapUserDtoToEntity(UserDTO userDTO);

}
