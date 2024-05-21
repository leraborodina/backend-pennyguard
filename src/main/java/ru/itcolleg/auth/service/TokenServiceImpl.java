package ru.itcolleg.auth.service;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;
    private final KeyPair keyPair;
    private String token;

    @Autowired
    public TokenServiceImpl() {
        this.keyPair = generateKeyPair();
    }

    public String generateToken(Long userId) {
        logger.info("Начало генерации токена");
        try {
            Date expirationDate = new Date(System.currentTimeMillis() + jwtExpirationMs);
            PrivateKey privateKey = keyPair.getPrivate();

            this.token = Jwts.builder()
                    .setSubject(userId.toString())
                    .setExpiration(expirationDate)
                    .signWith(privateKey, SignatureAlgorithm.RS256)
                    .compact();

            logger.info("Токен успешно сгенерирован");
            return this.token;
        } catch (Exception e) {
            logger.error("Ошибка при генерации токена", e);
            throw new RuntimeException("Ошибка при генерации токена", e);
        }
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public boolean verifyToken(String token, String publicKeyString) {
        logger.info("Начало верификации токена");
        try {
            PublicKey publicKey = stringToPublicKey(publicKeyString);

            try {
                Jws<Claims> jws = Jwts.parserBuilder()
                        .setSigningKey(publicKey)
                        .build()
                        .parseClaimsJws(token);

                Claims claims = jws.getBody();
                String subject = claims.getSubject();
                if (subject == null || subject.isEmpty()) {
                    return false;
                }
                Date expirationDate = claims.getExpiration();
                return expirationDate == null || !expirationDate.before(new Date());
            } catch (JwtException e) {
                return false;
            }
        } catch (Exception e) {
            logger.error("Ошибка при верификации токена", e);
            return false;
        }
    }

    public String getEncodedPublicKey() {
        logger.info("Получение закодированного открытого ключа");
        return encodePublicKey(keyPair.getPublic());
    }

    private KeyPair generateKeyPair() {
        logger.info("Начало генерации ключевой пары");
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom secureRandom = new SecureRandom();
            keyPairGenerator.initialize(2048, secureRandom);
            logger.info("Ключевая пара успешно сгенерирована");
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Ошибка при генерации ключевой пары", e);
            throw new RuntimeException("Ошибка при генерации ключевой пары", e);
        }
    }

    public String encodePublicKey(PublicKey publicKey) {
        logger.info("Кодирование открытого ключа");
        RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
        BigInteger modulus = rsaPublicKey.getModulus();
        BigInteger exponent = rsaPublicKey.getPublicExponent();
        String publicKeyString = modulus.toString() + "_" + exponent.toString();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(publicKeyString.getBytes(StandardCharsets.UTF_8));
    }

    public PublicKey stringToPublicKey(String encodedPublicKey) {
        logger.info("Конвертация строки в открытый ключ");
        try {
            byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedPublicKey);
            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
            RSAPublicKeySpec rsaPublicKeySpec = getRsaPublicKeySpec(decodedString);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(rsaPublicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("Ошибка при конвертации строки в открытый ключ", e);
            throw new RuntimeException("Ошибка при конвертации строки в открытый ключ", e);
        }
    }

    private static RSAPublicKeySpec getRsaPublicKeySpec(String decodedString) {
        logger.info("Получение спецификации открытого ключа RSA");
        String[] parts = decodedString.split("_");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Неверный формат закодированного открытого ключа");
        }
        BigInteger modulus = new BigInteger(parts[0]);
        BigInteger exponent = new BigInteger(parts[1]);
        return new RSAPublicKeySpec(modulus, exponent);
    }

    public Long extractUserIdFromToken(String token) {
        logger.info("Извлечение ID пользователя из токена");
        try {
            JwtParserBuilder parserBuilder = Jwts.parserBuilder()
                    .setSigningKey(keyPair.getPublic());
            token = token.replace("Bearer ", "");
            Claims claims = parserBuilder.build().parseClaimsJws(token).getBody();
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            logger.error("Ошибка при извлечении ID пользователя из токена", e);
            return null;
        }
    }

    public boolean validateJwtToken(String jwtToken) {
        logger.info("Проверка JWT токена");
        PublicKey publicKey = keyPair.getPublic();
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            logger.error("Ошибка при проверке JWT токена", e);
            return false;
        }
    }
}
