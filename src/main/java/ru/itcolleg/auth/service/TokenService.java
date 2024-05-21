package ru.itcolleg.auth.service;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

/**
 * Service interface for managing authentication tokens.
 * Сервисный интерфейс для управления аутентификационными токенами.
 */
public interface TokenService {

    /**
     * Generates a JWT token for the specified user ID.
     * Генерирует JWT-токен для указанного идентификатора пользователя.
     *
     * @param userId The ID of the user for whom the token is generated.
     *               Идентификатор пользователя, для которого создается токен.
     * @return The generated JWT token.
     * Сгенерированный JWT-токен.
     */
    String generateToken(Long userId) throws NoSuchAlgorithmException;

    /**
     * Encodes a PublicKey into a Base64-encoded string.
     * Кодирует открытый ключ в строку в кодировке Base64.
     *
     * @param publicKey The PublicKey to be encoded.
     *                  Открытый ключ для кодирования.
     * @return The Base64-encoded string representation of the PublicKey.
     * Строковое представление открытого ключа в кодировке Base64.
     */
    String encodePublicKey(PublicKey publicKey);

    /**
     * Retrieves the encoded representation of the public key.
     * Получает закодированное представление открытого ключа.
     *
     * @return The encoded string representation of the public key.
     * Закодированное строковое представление открытого ключа.
     */
    String getEncodedPublicKey();

    /**
     * Verifies the authenticity of a JWT token using the provided public key.
     * Проверяет подлинность JWT-токена с использованием предоставленного открытого ключа.
     *
     * @param token           The JWT token to be verified.
     *                        Проверяемый JWT-токен.
     * @param publicKeyString The encoded string representation of the public key.
     *                        Закодированное строковое представление открытого ключа.
     * @return true if the token is valid, false otherwise.
     * true, если токен действителен, в противном случае - false.
     */
    boolean verifyToken(String token, String publicKeyString);

    /**
     * Extracts the user ID from the JWT token.
     * Извлекает идентификатор пользователя из JWT-токена.
     *
     * @param token The JWT token from which to extract the user ID.
     *              JWT-токен, из которого нужно извлечь идентификатор пользователя.
     * @return The user ID extracted from the token.
     * Идентификатор пользователя, извлеченный из токена.
     */
    Long extractUserIdFromToken(String token);

    /**
     * Validates the authenticity and integrity of a JWT token.
     * Проверяет подлинность и целостность JWT-токена.
     *
     * @param jwtToken The JWT token to validate.
     *                 Проверяемый JWT-токен.
     * @return true if the token is valid, false otherwise.
     * true, если токен действителен, в противном случае - false.
     */
    boolean validateJwtToken(String jwtToken);

    /**
     * Retrieves the current token.
     * Получает текущий токен.
     *
     * @return The current token.
     * Текущий токен.
     */
    String getToken();
}
