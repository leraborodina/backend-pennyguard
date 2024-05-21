package ru.itcolleg.auth.exception;

/**
 * Exception thrown when a user attempts to access a resource without proper authorization.
 * Исключение, возникающее, когда пользователь пытается получить доступ к ресурсу без правильной авторизации.
 */
public class UnauthorizedAccessException extends RuntimeException {

    /**
     * Constructs a new UnauthorizedAccessException with the specified detail message.
     * Создает новый UnauthorizedAccessException с указанным сообщением об ошибке.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the getMessage() method.
     *                детализированное сообщение. Детализированное сообщение сохраняется для последующего получения с помощью метода getMessage().
     */
    public UnauthorizedAccessException(String message) {
        super(message);
    }

    /**
     * Constructs a new UnauthorizedAccessException with the specified detail message and cause.
     * Создает новый UnauthorizedAccessException с указанным сообщением об ошибке и причиной.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     *                детализированное сообщение (которое сохраняется для последующего получения с помощью метода getMessage()).
     * @param cause   the cause (which is saved for later retrieval by the getCause() method).
     *                причина (которая сохраняется для последующего получения с помощью метода getCause()).
     */
    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
