package ru.itcolleg.auth.service;

import ru.itcolleg.user.exception.UserLoginCredentialsNotCorrect;
import ru.itcolleg.user.exception.UserNotFoundException;

/**
 * The AuthService interface provides methods for user authentication.
 * Интерфейс AuthService предоставляет методы для аутентификации пользователей.
 */
public interface AuthService {

    /**
     * Authenticates a user based on the provided username and password.
     * Throws exceptions UserNotFoundException and UserLoginCredentialsNotCorrect to handle different error scenarios.
     * Аутентифицирует пользователя на основе предоставленного имени пользователя и пароля.
     * Выбрасывает исключения UserNotFoundException и UserLoginCredentialsNotCorrect для обработки различных сценариев ошибок.
     *
     * @param username The username (email) of the user.
     *                 Имя пользователя (email) пользователя.
     * @param password The password of the user.
     *                 Пароль пользователя.
     * @return true if the user's credentials are correct, otherwise false.
     * true, если учетные данные пользователя верны, в противном случае false.
     * @throws UserNotFoundException          If the specified user is not found during authentication.
     *                                        Если указанный пользователь не найден во время аутентификации.
     * @throws UserLoginCredentialsNotCorrect If the provided login credentials are incorrect.
     *                                        Если предоставленные учетные данные для входа неверны.
     */
    boolean authenticateUser(String username, String password) throws UserNotFoundException, UserLoginCredentialsNotCorrect;
}
