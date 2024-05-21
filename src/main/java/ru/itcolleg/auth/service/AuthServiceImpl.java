package ru.itcolleg.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itcolleg.user.exception.UserLoginCredentialsNotCorrect;
import ru.itcolleg.user.exception.UserNotFoundException;
import ru.itcolleg.user.model.User;
import ru.itcolleg.user.repository.UserRepository;

import java.util.Optional;

/**
 * Реализация интерфейса AuthService для аутентификации пользователей.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean authenticateUser(String email, String password) throws UserNotFoundException, UserLoginCredentialsNotCorrect {
        logger.info("Аутентификация пользователя с адресом электронной почты: {}", email);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String hashedPassword = user.getPassword();
            if (passwordEncoder.matches(password, hashedPassword)) {
                return true;
            } else {
                throw new UserLoginCredentialsNotCorrect("Неверный пароль.");
            }
        } else {
            throw new UserNotFoundException("Пользователь с адресом электронной почты не найден: " + email);
        }
    }
}
