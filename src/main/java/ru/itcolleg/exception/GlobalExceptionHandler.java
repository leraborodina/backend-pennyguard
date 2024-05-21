package ru.itcolleg.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.itcolleg.auth.exception.UnauthorizedAccessException;

/**
 * Global exception handler for handling exceptions across all controllers.
 * Глобальный обработчик исключений для обработки исключений во всех контроллерах.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles UnauthorizedAccessException and returns a ResponseEntity with status code 403 (FORBIDDEN).
     * Обрабатывает UnauthorizedAccessException и возвращает ResponseEntity с кодом состояния 403 (FORBIDDEN).
     *
     * @param ex The UnauthorizedAccessException to handle.
     *           Необработанное исключение UnauthorizedAccessException.
     *
     * @return ResponseEntity with status code 403 and the exception message in the body.
     * ResponseEntity с кодом состояния 403 и сообщением об исключении в теле.
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<String> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }
}
