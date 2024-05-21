package ru.itcolleg.auth.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itcolleg.auth.exception.UnauthorizedAccessException;
import ru.itcolleg.auth.service.TokenService;

/**
 * Аспект для проверки валидности токена авторизации.
 * Aspect for validating the validity of the authorization token.
 */
@Aspect
@Component
public class TokenValidationAspect {

    private static final Logger logger = LoggerFactory.getLogger(TokenValidationAspect.class);

    private final TokenService tokenService;

    @Autowired
    public TokenValidationAspect(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Определение точки среза для классов, помеченных аннотацией RequiresTokenValidation.
     * Definition of a pointcut for classes annotated with RequiresTokenValidation.
     */
    @Pointcut("@within(requiresTokenValidation)")
    public void requiresTokenValidationClass(RequiresTokenValidation requiresTokenValidation) {
    }

    /**
     * Проверка валидности токена перед выполнением метода, помеченного аннотацией RequiresTokenValidation.
     * Checks the validity of the token before executing a method annotated with RequiresTokenValidation.
     *
     * @param requiresTokenValidation Аннотация RequiresTokenValidation на методе.
     * @param token                   Токен авторизации из заголовка запроса.
     */
    @Before("requiresTokenValidationClass(requiresTokenValidation) && args(.., token)")
    public void validateToken(RequiresTokenValidation requiresTokenValidation, String token) {
        logger.info("Проверка валидности токена перед выполнением метода");

        if (token == null || !tokenService.validateJwtToken(token)) {
            throw new UnauthorizedAccessException("Вы не авторизованы для доступа к этому ресурсу");
        }
    }

}
