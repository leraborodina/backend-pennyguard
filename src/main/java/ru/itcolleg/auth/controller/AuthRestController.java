package ru.itcolleg.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itcolleg.auth.dto.LoginRequest;
import ru.itcolleg.auth.dto.LoginResponse;
import ru.itcolleg.auth.service.AuthService;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.user.exception.UserLoginCredentialsNotCorrect;
import ru.itcolleg.user.exception.UserNotFoundException;
import ru.itcolleg.user.model.User;
import ru.itcolleg.user.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Optional;

/**
 * Controller class handling authentication-related endpoints.
 * Класс контроллера, обрабатывающий конечные точки, связанные с аутентификацией.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
    private static final Logger logger = LoggerFactory.getLogger(AuthRestController.class);
    private final UserService userService;
    private final AuthService authService;
    private final TokenService tokenService;

    @Autowired
    public AuthRestController(UserService userService, AuthService authService, TokenService tokenService) {
        this.userService = userService;
        this.authService = authService;
        this.tokenService = tokenService;
    }

    /**
     * Endpoint for refreshing authentication token.
     * Точка доступа для обновления аутентификационного токена.
     *
     * @param userId User ID for which the token needs to be refreshed.
     *               Идентификатор пользователя, для которого необходимо обновить токен.
     * @return ResponseEntity with the new access token.
     * ResponseEntity с новым токеном доступа.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("X-UserId") Long userId) {
        logger.info("Запрос на обновление токена: userId={}", userId);
        try {
            String newAccessToken = tokenService.generateToken(userId);
            userService.updateUserPublicKey(userId, newAccessToken);
            return ResponseEntity.ok(Collections.singletonMap("token", newAccessToken));
        } catch (Exception e) {
            logger.error("Ошибка обновления токена: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка обновления токена");
        }
    }

    /**
     * Endpoint for user login.
     * Точка доступа для входа пользователя.
     *
     * @param loginRequest Login request containing user credentials.
     *                     Запрос на вход, содержащий учетные данные пользователя.
     * @return ResponseEntity with the user's authentication token.
     * ResponseEntity с аутентификационным токеном пользователя.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Запрос на вход пользователя: {}", loginRequest.getEmail());
        try {
            boolean loggedIn = authService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());

            if (loggedIn) {
                Optional<User> optionalUser = userService.getUserByEmail(loginRequest.getEmail());

                if (optionalUser.isPresent()) {
                    String token = tokenService.generateToken(optionalUser.get().getId());
                    userService.updateUserPublicKey(optionalUser.get().getId(), tokenService.getEncodedPublicKey());

                    LoginResponse response = new LoginResponse();
                    response.setFirstname(optionalUser.get().getFirstname());
                    response.setLastname(optionalUser.get().getLastname());
                    response.setToken(token);

                    return ResponseEntity.ok(response);
                } else {
                    logger.error("Информация о пользователе недоступна");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Информация о пользователе недоступна");
                }
            }

            logger.error("Ошибка аутентификации");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ошибка аутентификации");
        } catch (UserNotFoundException e) {
            logger.error("Пользователь не найден: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserLoginCredentialsNotCorrect e) {
            logger.error("Неверные учетные данные: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Внутренняя ошибка сервера: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
        }
    }

    /**
     * Endpoint for user logout.
     * Точка доступа для выхода пользователя.
     *
     * @param request  HttpServletRequest object.
     *                 Объект HttpServletRequest.
     * @param response HttpServletResponse object.
     *                 Объект HttpServletResponse.
     * @return ResponseEntity indicating the status of the logout operation.
     * ResponseEntity, указывающий статус операции выхода из системы.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Запрос на выход пользователя");
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    return ResponseEntity.ok("Выход успешно выполнен");
                }
            }
        }

        logger.error("Пользователь не вошел в систему");
        return ResponseEntity.ok("Пользователь не вошел в систему");
    }
}
