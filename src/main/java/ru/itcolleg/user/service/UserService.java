package ru.itcolleg.user.service;

import ru.itcolleg.auth.dto.LoginResponse;
import ru.itcolleg.user.dto.UserDTO;
import ru.itcolleg.user.exception.UserAlreadyExistsException;
import ru.itcolleg.user.exception.UserNotFoundException;
import ru.itcolleg.user.model.User;

import java.util.Optional;

/**
 * Service interface for managing user-related operations.
 * Интерфейс сервиса для управления операциями, связанными с пользователями.
 */
public interface UserService {

    /**
     * Saves a new user in the system.
     * Сохраняет нового пользователя в системе.
     *
     * @param userDTO The user information to save.
     *                Информация о пользователе для сохранения.
     * @return An Optional containing the login response if the user is saved successfully, otherwise empty.
     * Optional, содержащий ответ на вход, если пользователь успешно сохранен, в противном случае - пустой.
     * @throws UserAlreadyExistsException If a user with the provided email already exists.
     *                                    Если пользователь с предоставленным адресом электронной почты уже существует.
     */
    Optional<LoginResponse> saveUser(UserDTO userDTO) throws UserAlreadyExistsException;

    /**
     * Gets a user by email.
     * Получает пользователя по адресу электронной почты.
     *
     * @param email The email of the user.
     *              Адрес электронной почты пользователя.
     * @return An Optional containing the user if found, otherwise empty.
     * Optional, содержащий пользователя, если он найден, в противном случае - пустой.
     * @throws UserNotFoundException If the user with the given email is not found.
     *                               Если пользователь с указанным адресом электронной почты не найден.
     */
    Optional<User> getUserByEmail(String email) throws UserNotFoundException;

    /**
     * Gets the public key of a user by email.
     * Получает открытый ключ пользователя по адресу электронной почты.
     *
     * @param email The email of the user.
     *              Адрес электронной почты пользователя.
     * @return The public key if found.
     * Открытый ключ, если найден.
     * @throws UserNotFoundException If the user with the provided email is not found.
     *                               Если пользователь с предоставленным адресом электронной почты не найден.
     */
    String getPublicKeyByEmail(String email) throws UserNotFoundException;

    /**
     * Updates the public key of a user.
     * Обновляет открытый ключ пользователя.
     *
     * @param userId    The userId of the user to update.
     *                  Идентификатор пользователя для обновления.
     * @param publicKey The new public key.
     *                  Новый открытый ключ.
     * @throws IllegalArgumentException If the provided user is null.
     *                                  Если предоставленный пользователь равен null.
     */
    void updateUserPublicKey(Long userId, String publicKey);

    /**
     * Gets a user by their ID.
     * Получает пользователя по его идентификатору.
     *
     * @param userId The ID of the user.
     *               Идентификатор пользователя.
     * @return An Optional containing the user if found, otherwise empty.
     * Optional, содержащий пользователя, если он найден, в противном случае - пустой.
     */
    Optional<User> getUserById(Long userId) throws UserNotFoundException;
}
