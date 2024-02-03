package ru.itcolleg.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itcolleg.user.exception.UserLoginCredentialsNotCorrect;
import ru.itcolleg.user.exception.UserNotFoundException;
import ru.itcolleg.user.model.User;
import ru.itcolleg.user.repository.UserRepository;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean authenticateUser(String email, String password) throws UserNotFoundException, UserLoginCredentialsNotCorrect {
        // Step 1: Retrieve user information by email from the repository
        Optional<User> optionalUser = userRepository.findByEmail(email);

        // Step 2: Check if the user is found in the repository
        if (optionalUser.isPresent()) {
            // Step 3: Get the user object
            User user = optionalUser.get();

            // Step 4: Retrieve the stored hashed password
            String hashedPwd = user.getPassword();

            // Step 5: Use the PasswordEncoder to check if the provided password matches the stored hashed password
            if (passwordEncoder.matches(password, hashedPwd)) {
                // Step 6: If passwords match, authentication is successful
                return true;
            } else {
                // Step 7: If passwords do not match, throw an exception
                throw new UserLoginCredentialsNotCorrect("Email or password is wrong.");
            }
        } else {
            // Step 8: If user is not found, throw an exception
            throw new UserNotFoundException("User not found for email: " + email);
        }
    }
}
