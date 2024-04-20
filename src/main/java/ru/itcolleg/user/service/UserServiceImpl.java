package ru.itcolleg.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.user.dto.UserDTO;
import ru.itcolleg.auth.dto.LoginResponse;
import ru.itcolleg.user.exception.UserAlreadyExistsException;
import ru.itcolleg.user.exception.UserNotFoundException;
import ru.itcolleg.user.mapper.UserMapper;
import ru.itcolleg.user.model.User;
import ru.itcolleg.user.repository.UserRepository;

import java.util.Optional;

/**
 * Implementation of the UserService interface providing user-related operations.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<LoginResponse> saveUser(UserDTO userDTO) throws UserAlreadyExistsException {
        // Step 1: Extract email from UserDTO
        String email = userDTO.getEmail();

        // Step 2: Check if the email already exists in the database
        if (userRepository.existsByEmail(email)) {
            // Step 3: Throw UserAlreadyExistsException if email exists
            throw new UserAlreadyExistsException("Email already exists");
        }

        // Step 4: Create a new User entity
        User user = new User();

        // Step 5: Set user properties from UserDTO
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setEmail(userDTO.getEmail());

        // Step 6: Hash the password using BCryptPasswordEncoder
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(hashedPassword);

        // Step 7: Save the user in the database
        User dbUser = userRepository.save(user);

        // Step 8: Map the saved user to LoginResponse using UserMapper
        LoginResponse savedUser = UserMapper.mapUserToUserResponse(dbUser);

        // Step 9: Return an Optional containing the saved user, or empty if null
        return Optional.ofNullable(savedUser);
    }

    @Override
    public Optional<User> getUserByEmail(String email) throws UserNotFoundException {
        // Step 1: Retrieve the user from the repository using the provided email
        Optional<User> userOptional = userRepository.findByEmail(email);

        // Step 2: Check if the user is present in the Optional
        if (userOptional.isPresent()) {
            // Step 3: Return the user if found
            return userOptional;
        } else {
            // Step 4: If the user is not found, throw UserNotFoundException
            throw new UserNotFoundException("User not found for email: " + email);
        }
    }


    @Override
    public String getPublicKeyByEmail(String email) throws UserNotFoundException {
        try {
            // Step 1: Retrieve the user from the repository using the provided email
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("User not found for email: " + email));

            // Step 2: Retrieve and return the public key
            String publicKey = user.getPublicKey(); // Adjust this based on your actual User entity

            // Step 3: Check if the retrieved public key is not null or empty
            if (publicKey == null || publicKey.isEmpty()) {
                throw new UserNotFoundException("Public key not found for user with email: " + email);
            }

            // Step 4: Return the retrieved public key
            return publicKey;
        } catch (UserNotFoundException e) {
            // Step 5: Handle and rethrow the exception if needed
            throw e;
        } catch (Exception e) {
            // Step 6: Handle unexpected exceptions during public key retrieval
            // Log or perform specific actions for unexpected exceptions
            throw new UserNotFoundException("Error retrieving public key for this user: " + e);
        }
    }

    @Override
    public void updateUserPublicKey(Long userId, String publicKey) {
        try {
            // Step 1: Check if the provided user ID is not null
            if (userId != null) {
                // Step 2: Fetch the user by ID and update the user's public key
                Optional<User> optionalUser = this.userRepository.findById(userId);

                if(optionalUser.isPresent()){
                    User user = optionalUser.get();
                    user.setPublicKey(publicKey);
                    userRepository.save(user);
                }
            } else {
                // Step 4: Handle the case when the provided user is null
                throw new IllegalArgumentException("User cannot be null for updating public key");
            }
        } catch (IllegalArgumentException e) {
            // Step 5: Handle and rethrow the IllegalArgumentException if needed
            throw e;
        } catch (Exception e) {
            // Step 6: Handle unexpected exceptions during public key update
            // Log or perform specific actions for unexpected exceptions
            throw new RuntimeException("Error updating public key for user", e);
        }
    }

}
