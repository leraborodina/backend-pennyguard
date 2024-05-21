package ru.itcolleg.transaction.exception;

/**
 * Custom exception to indicate that a category was not found.
 * Исключение, указывающее на то, что категория не была найдена.
 */
public class CategoryNotFoundException extends RuntimeException {

    /**
     * Constructs a new CategoryNotFoundException with the specified detail message.
     * Создает новое исключение CategoryNotFoundException с указанным детальным сообщением.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     *                детальное сообщение (которое сохраняется для последующего извлечения с помощью метода getMessage ())
     */
    public CategoryNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new CategoryNotFoundException with the specified detail message and cause.
     * Создает новое исключение CategoryNotFoundException с указанным детальным сообщением и причиной.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     *                детальное сообщение (которое сохраняется для последующего извлечения с помощью метода getMessage ())
     * @param cause   the cause (which is saved for later retrieval by the getCause() method)
     *                причина (которая сохраняется для последующего извлечения с помощью метода getCause ())
     */
    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
