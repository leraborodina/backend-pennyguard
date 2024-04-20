package ru.itcolleg.auth.service;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

/**
 * Service interface for managing authentication tokens.
 */
public interface TokenService {

    /**
     * Generates a JWT token for the specified user ID.
     *
     * @param userId The ID of the user for whom the token is generated.
     * @return The generated JWT token.
     */
    String generateToken(Long userId) throws NoSuchAlgorithmException;

    /**
     * Encodes a PublicKey into a Base64-encoded string.
     *
     * @param publicKey The PublicKey to be encoded.
     * @return The Base64-encoded string representation of the PublicKey.
     */
    String encodePublicKey(PublicKey publicKey);

    /**
     * Retrieves the encoded representation of the public key.
     *
     * @return The encoded string representation of the public key.
     */
    String getEncodedPublicKey();

    /**
     * Verifies the authenticity of a JWT token using the provided public key.
     *
     * @param token           The JWT token to be verified.
     * @param publicKeyString The encoded string representation of the public key.
     * @return true if the token is valid, false otherwise.
     */
    boolean verifyToken(String token, String publicKeyString);

    Long extractUserIdFromToken(String token);

    boolean validateJwtToken(String jwtToken);
}
