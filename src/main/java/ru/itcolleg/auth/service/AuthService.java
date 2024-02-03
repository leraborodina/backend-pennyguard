package ru.itcolleg.auth.service;

import ru.itcolleg.user.exception.UserLoginCredentialsNotCorrect;
import ru.itcolleg.user.exception.UserNotFoundException;

public interface AuthService {

    /**
     * This method is responsible for authenticating a user based on the provided username and password.
     * It throws exceptions UserNotFoundException and UserLoginCredentialsNotCorrect to handle different error scenarios.
     * @param username email
     * @param password password
     * @return true, if users credentials are correct
     * @throws UserNotFoundException If the specified user is not found during authentication.
     * @throws UserLoginCredentialsNotCorrect If the provided login credentials are incorrect.
     */
    boolean authenticateUser(String username, String password) throws UserNotFoundException, UserLoginCredentialsNotCorrect;
}
