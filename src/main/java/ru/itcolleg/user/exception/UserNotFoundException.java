/**
 * Exception thrown when a user is not found during a retrieval operation.
 */
package ru.itcolleg.user.exception;

public class UserNotFoundException extends Exception {
    /**
     * Constructs a new UserNotFoundException with the specified detail message.
     *
     * @param message the detail message.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
