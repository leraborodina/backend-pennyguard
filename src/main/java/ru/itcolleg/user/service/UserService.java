package ru.itcolleg.user.service;

import ru.itcolleg.user.dto.UserDTO;
import ru.itcolleg.auth.dto.LoginResponse;
import ru.itcolleg.user.exception.UserAlreadyExistsException;
import ru.itcolleg.user.exception.UserNotFoundException;
import ru.itcolleg.user.model.User;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface UserService {
    /**
     * Saves a new user in the system.
     *
     * @param userDTO The user information to save.
     * @return An Optional containing the login response if the user is saved successfully, otherwise empty.
     * @throws UserAlreadyExistsException If a user with the provided email already exists.
     */
    Optional<LoginResponse> saveUser(UserDTO userDTO) throws UserAlreadyExistsException;

    /**
     * Gets a user by email.
     *
     * @param email The email of the user.
     * @return An Optional containing the user if found, otherwise empty.
     * @throws UserNotFoundException If the user with the given email is not found.
     */
    Optional<User> getUserByEmail(String email) throws UserNotFoundException;

    /**
     * Gets the public key of a user by email.
     *
     * @param email The email of the user.
     * @return The public key if found.
     * @throws UserNotFoundException If the user with the provided email is not found.
     */
    String getPublicKeyByEmail(String email) throws UserNotFoundException;

    /**
     * Updates the public key of a user.
     *
     * @param user     The user to update.
     * @param publicKey The new public key.
     * @throws IllegalArgumentException If the provided user is null.
     */
    void updateUserPublicKey(User user, String publicKey);
}