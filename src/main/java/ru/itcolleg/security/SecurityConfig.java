package ru.itcolleg.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Конфигурация безопасности приложения.
 * Security configuration of the application.
 */
@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * Определяет бин для BCryptPasswordEncoder, который является кодировщиком пароля,
     * обычно используемым в Spring Security для безопасного хеширования пароля.
     * <p>
     * Defines a bean for BCryptPasswordEncoder, which is a password encoder
     * commonly used in Spring Security for secure password hashing.
     *
     * @return BCryptPasswordEncoder bean
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        logger.info("Creating BCryptPasswordEncoder bean.");
        return new BCryptPasswordEncoder();
    }
}
