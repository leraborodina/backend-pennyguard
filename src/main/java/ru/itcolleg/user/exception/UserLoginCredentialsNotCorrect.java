/**
 * Exception thrown when user login credentials are not correct during authentication.
 */
package ru.itcolleg.user.exception;

public class UserLoginCredentialsNotCorrect extends Throwable {
    /**
     * Constructs a new UserLoginCredentialsNotCorrect exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public UserLoginCredentialsNotCorrect(String message) {
        super(message);
    }
}
