package ru.itcolleg.auth.aspect;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itcolleg.auth.exception.UnauthorizedAccessException;
import ru.itcolleg.auth.service.RequiresTokenValidation;
import ru.itcolleg.auth.service.TokenService;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class TokenValidationAspect {

    private final TokenService tokenService;

    @Autowired
    public TokenValidationAspect(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Before("@annotation(requiresTokenValidation) && args(.., token)")
    public void validateToken(RequiresTokenValidation requiresTokenValidation, String token) {
        if (token == null || !tokenService.validateJwtToken(token)) {
            throw new UnauthorizedAccessException("You are not authorized to access this resource");
        }
    }
}
