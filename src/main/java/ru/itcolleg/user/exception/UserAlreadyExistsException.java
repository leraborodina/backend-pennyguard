/**
 * Exception thrown when attempting to create a user with an email that already exists.
 */
package ru.itcolleg.user.exception;

public class UserAlreadyExistsException extends Exception {
    /**
     * Constructs a new UserAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message.
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
