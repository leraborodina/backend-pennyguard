package ru.itcolleg.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itcolleg.auth.dto.LoginResponse;
import ru.itcolleg.user.dto.UserDTO;
import ru.itcolleg.user.exception.UserAlreadyExistsException;
import ru.itcolleg.user.service.UserService;

import java.util.Optional;

/**
 * Controller for handling user-related operations.
 * Контроллер для обработки операций, связанных с пользователями.
 */
@RestController
@RequestMapping("/api/user")
public class UserRestController {
    private final Logger logger = LoggerFactory.getLogger(UserRestController.class);
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     * Регистрирует нового пользователя.
     *
     * @param userDTO The user information to be registered.
     * @return ResponseEntity containing the saved user information or an error response.
     * Возвращает ResponseEntity, содержащий информацию о сохраненном пользователе или ответ об ошибке.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        logger.info("Начало процесса регистрации пользователя.");
        try {
            Optional<LoginResponse> savedUser = userService.saveUser(userDTO);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            logger.error("Ошибка регистрации пользователя: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Произошла внутренняя ошибка сервера во время регистрации пользователя: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
