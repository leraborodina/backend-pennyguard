package ru.itcolleg.user.exception;

/**
 * Exception thrown when user login credentials are not correct during authentication.
 * Исключение, возникающее, когда учетные данные пользователя неверны во время аутентификации.
 */
public class UserLoginCredentialsNotCorrect extends Exception {
    /**
     * Constructs a new UserLoginCredentialsNotCorrect exception with the specified detail message.
     * Создает новое исключение UserLoginCredentialsNotCorrect с указанным детальным сообщением.
     *
     * @param message the detail message.
     *                детальное сообщение.
     */
    public UserLoginCredentialsNotCorrect(String message) {
        super(message);
    }
}
