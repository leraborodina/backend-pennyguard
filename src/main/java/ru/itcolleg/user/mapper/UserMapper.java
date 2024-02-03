/**
 * Mapper class responsible for converting User entities to LoginResponse DTOs.
 */
package ru.itcolleg.user.mapper;

import org.modelmapper.ModelMapper;
import ru.itcolleg.auth.dto.LoginResponse;
import ru.itcolleg.user.model.User;

public class UserMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    /**
     * Maps a User entity to a LoginResponse DTO.
     *
     * @param user the User entity to be mapped.
     * @return the corresponding LoginResponse DTO.
     */
    public static LoginResponse mapUserToUserResponse(User user) {
        return modelMapper.map(user, LoginResponse.class);
    }
}
