package ru.itcolleg.auth.service;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Date;


@Service
public class TokenServiceImpl implements TokenService {


    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private KeyPair keyPair;

    /**
     * Constructor for initializing the TokenService with a generated key pair.
     */
    @Autowired
    public TokenServiceImpl() {
        try {
            this.keyPair = generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating key pair", e);
        }
    }

    public String generateToken(Long userId) {
        try {
            // Step 1: Set the expiration date for the token
            Date expirationDate = new Date(System.currentTimeMillis() + jwtExpirationMs);

            // Step 2: Use the private key for signing
            PrivateKey privateKey = keyPair.getPrivate();

            // Step 3: Build and sign the JWT token
            return Jwts.builder()
                    .setSubject(userId.toString())
                    .setExpiration(expirationDate)
                    .signWith(SignatureAlgorithm.RS256, privateKey)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    @Override
    public boolean verifyToken(String token, String publicKeyString) {
        try {
            // Step 1: Convert the encoded public key string to a PublicKey object
            PublicKey publicKey = stringToPublicKey(publicKeyString);

            try {
                // Step 2: Parse and verify the JWT token using the public key
                Jws<Claims> jws = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
                Claims claims = jws.getBody();

                // Step 3: Optionally, perform additional verification checks here based on claims
                String subject = claims.getSubject();
                // Example additional check: Ensure the subject is not empty
                if (subject == null || subject.isEmpty()) {
                    // Handle the case where the subject is missing or empty
                    return false;
                }

                // TODO check token expiration date

                // Step 4: Token is valid
                return true;
            } catch (ExpiredJwtException e) {
                // Step 5: Handle the case where the token has expired
                // You might want to log or perform specific actions for expired tokens
                return false;
            } catch (JwtException e) {
                // Step 6: Handle general JWT exceptions
                // This includes issues like invalid signature, invalid format, etc.
                // You might want to log or perform specific actions for these cases
                return false;
            }
        } catch (Exception e) {
            // Step 7: Handle unexpected exceptions during the public key conversion
            // This includes issues like invalid key format, invalid base64 encoding, etc.
            // Log or perform specific actions for unexpected exceptions
            return false;
        }
    }


    public String getEncodedPublicKey() {
        return encodePublicKey(keyPair.getPublic());
    }

    /**
     * Generates a new RSA KeyPair.
     *
     * @return The generated KeyPair.
     * @throws NoSuchAlgorithmException If the RSA algorithm is not available.
     */
    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        // Step 1: Create a KeyPairGenerator instance for RSA
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        // Step 2: Initialize the KeyPairGenerator with the desired key size and a SecureRandom instance
        //         for generating secure random numbers
        SecureRandom secureRandom = new SecureRandom();
        keyPairGenerator.initialize(2048, secureRandom);

        // Step 3: Generate the KeyPair
        return keyPairGenerator.generateKeyPair();
    }


    /**
     * Encodes a PublicKey into a Base64-encoded string.
     *
     * @param publicKey The PublicKey to be encoded.
     * @return The Base64-encoded string representation of the PublicKey.
     */
    public String encodePublicKey(PublicKey publicKey) {
        // Step 1: Cast the PublicKey to RSAPublicKey for access to RSA-specific methods
        RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;

        // Step 2: Retrieve the modulus and public exponent from the RSAPublicKey
        BigInteger modulus = rsaPublicKey.getModulus();
        BigInteger exponent = rsaPublicKey.getPublicExponent();

        // Step 3: Concatenate the modulus and exponent into a string with an underscore separator
        String publicKeyString = modulus.toString() + "_" + exponent.toString();

        // Step 4: Encode the concatenated string into Base64
        return Base64.getEncoder().encodeToString(publicKeyString.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Converts a Base64-encoded string representation of a public key into a PublicKey object.
     *
     * @param encodedPublicKey The Base64-encoded string representation of the public key.
     * @return The PublicKey created from the encoded string.
     * @throws NoSuchAlgorithmException If the RSA algorithm is not available.
     * @throws InvalidKeySpecException  If the provided key specification is invalid.
     * @throws IllegalArgumentException If the encoded public key has an invalid format.
     */
    public PublicKey stringToPublicKey(String encodedPublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Step 1: Decode the Base64-encoded string into bytes
        byte[] decodedBytes = Base64.getDecoder().decode(encodedPublicKey);

        // Step 2: Convert the byte array into a UTF-8 encoded string
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);

        // Step 3: Split the decoded string into modulus and exponent using the underscore separator
        RSAPublicKeySpec rsaPublicKeySpec = getRsaPublicKeySpec(decodedString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // Step 7: Generate the public key using the RSA public key specification
        return keyFactory.generatePublic(rsaPublicKeySpec);
    }

    /**
     * Parses the decoded string into an RSAPublicKeySpec.
     *
     * @param decodedString The decoded string containing modulus and exponent.
     * @return RSAPublicKeySpec created from the decoded string.
     * @throws IllegalArgumentException If the decoded string has an invalid format.
     */
    private static RSAPublicKeySpec getRsaPublicKeySpec(String decodedString) {
        // Step 4: Split the decoded string into modulus and exponent using the underscore separator
        String[] parts = decodedString.split("_");
        if (parts.length != 2) {
            // Step 5: Handle the case where the encoded public key has an invalid format
            throw new IllegalArgumentException("Invalid encoded public key format");
        }

        // Step 6: Convert modulus and exponent strings into BigIntegers
        BigInteger modulus = new BigInteger(parts[0]);
        BigInteger exponent = new BigInteger(parts[1]);

        // Step 7: Create an RSA public key using the modulus and exponent
        return new RSAPublicKeySpec(modulus, exponent);
    }


}
