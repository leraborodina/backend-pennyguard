package ru.itcolleg.user.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itcolleg.auth.dto.LoginResponse;
import ru.itcolleg.user.dto.UserDTO;
import ru.itcolleg.user.exception.UserAlreadyExistsException;
import ru.itcolleg.user.exception.UserNotFoundException;
import ru.itcolleg.user.mapper.UserMapper;
import ru.itcolleg.user.model.User;
import ru.itcolleg.user.repository.UserRepository;

import java.util.Optional;

import static ru.itcolleg.user.util.UserConstants.*;

/**
 * Service implementation of the UserService interface, providing operations related to users.
 * <p>
 * Реализация сервиса интерфейса UserService, предоставляющая операции, связанные с пользователями.
 */
@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<LoginResponse> saveUser(UserDTO userDTO) throws UserAlreadyExistsException {
        logger.info("Начало процесса сохранения пользователя");

        String email = userDTO.getEmail();
        if (userRepository.existsByEmail(email)) {
            logger.error(EMAIL_ALREADY_EXISTS + " {}", email);
            throw new UserAlreadyExistsException(EMAIL_ALREADY_EXISTS);
        }

        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());

        User user = userMapper.mapUserDtoToEntity(userDTO);
        user.setPassword(hashedPassword);

        User savedUser = userRepository.save(user);

        LoginResponse userDetails = userMapper.mapUserToLoginResponse(savedUser);

        logger.info("Процесс сохранения пользователя завершен");
        return Optional.ofNullable(userDetails);
    }

    @Override
    public Optional<User> getUserByEmail(String email) throws UserNotFoundException {
        logger.info("Начало процесса извлечения пользователя по электронной почте");

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            logger.info("Процесс извлечения пользователя по электронной почте завершен");
            return userOptional;
        } else {
            logger.error(USER_NOT_FOUND_EMAIL + " {}", email);
            throw new UserNotFoundException(USER_NOT_FOUND_EMAIL);
        }
    }

    @Override
    public String getPublicKeyByEmail(String email) throws UserNotFoundException {
        logger.info("Начало процесса извлечения открытого ключа по электронной почте");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error(USER_NOT_FOUND_EMAIL + " {}", email);
                    return new UserNotFoundException(USER_NOT_FOUND_EMAIL + " " + email);
                });

        String publicKey = Optional.ofNullable(user.getPublicKey())
                .orElseThrow(() -> new IllegalStateException(PUBLIC_KEY_NOT_FOUND_EMAIL));

        logger.info("Процесс извлечения открытого ключа по электронной почте завершен");
        return publicKey;
    }

    @Override
    public void updateUserPublicKey(Long userId, String publicKey) {
        logger.info("Начало процесса обновления открытого ключа пользователя");

        if (userId == null) {
            logger.error(USER_ID_NULL);
            throw new IllegalArgumentException(USER_ID_NULL);
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPublicKey(publicKey);
            userRepository.save(user);
            logger.info("Процесс обновления открытого ключа пользователя завершен");
        } else {
            logger.error(USER_NOT_FOUND_ID + " {}", userId);
            throw new RuntimeException(USER_NOT_FOUND_ID + " " + userId);
        }
    }

    @Override
    public Optional<User> getUserById(Long userId) throws UserNotFoundException {
        logger.info("Начало процесса извлечения пользователя по идентификатору");
        if (userId == null) {
            logger.error(USER_ID_NULL);
            throw new IllegalArgumentException(USER_ID_NULL);
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            logger.info("Процесс извлечения пользователя по идентификатору завершен");
            return userOptional;
        } else {
            logger.error(USER_NOT_FOUND_ID + " " + userId);
            throw new UserNotFoundException(USER_NOT_FOUND_ID + " " + userId);
        }
    }
}

