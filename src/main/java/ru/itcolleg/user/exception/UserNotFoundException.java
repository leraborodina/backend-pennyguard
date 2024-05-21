package ru.itcolleg.user.exception;

/**
 * Exception thrown when a user is not found during a retrieval operation.
 * Исключение, возникающее, когда пользователь не найден во время операции извлечения.
 */
public class UserNotFoundException extends Exception {
    /**
     * Constructs a new UserNotFoundException with the specified detail message.
     * Создает новое исключение UserNotFoundException с указанным детальным сообщением.
     *
     * @param message the detail message.
     *                детальное сообщение.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}