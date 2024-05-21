package ru.itcolleg.user.exception;

/**
 * Exception thrown when attempting to create a user with an email that already exists.
 * Исключение, возникающее при попытке создать пользователя с уже существующим адресом электронной почты.
 */
public class UserAlreadyExistsException extends Exception {
    /**
     * Constructs a new UserAlreadyExistsException with the specified detail message.
     * Создает новое исключение UserAlreadyExistsException с указанным детальным сообщением.
     *
     * @param message the detail message.
     *                детальное сообщение.
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}