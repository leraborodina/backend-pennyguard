package ru.itcolleg.user.dto;

/**
 * Data Transfer Object (DTO) for representing user information during registration.
 * Объект передачи данных (DTO) для представления информации о пользователе во время регистрации.
 */
public class UserDTO {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String publicKey;

    /**
     * Default constructor.
     * Конструктор по умолчанию.
     */
    public UserDTO() {
    }

    /**
     * Get the user's first name.
     * Получить имя пользователя.
     *
     * @return The first name.
     * Имя пользователя.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Set the user's first name.
     * Установить имя пользователя.
     *
     * @param firstname The first name to set.
     *                  Имя пользователя для установки.
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Get the user's last name.
     * Получить фамилию пользователя.
     *
     * @return The last name.
     * Фамилия пользователя.
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Set the user's last name.
     * Установить фамилию пользователя.
     *
     * @param lastname The last name to set.
     *                 Фамилия пользователя для установки.
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Get the user's email address.
     * Получить адрес электронной почты пользователя.
     *
     * @return The email address.
     * Адрес электронной почты пользователя.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the user's email address.
     * Установить адрес электронной почты пользователя.
     *
     * @param email The email address to set.
     *              Адрес электронной почты пользователя для установки.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the user's password.
     * Получить пароль пользователя.
     *
     * @return The password.
     * Пароль пользователя.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the user's password.
     * Установить пароль пользователя.
     *
     * @param password The password to set.
     *                 Пароль пользователя для установки.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the user's public key.
     * Получить открытый ключ пользователя.
     *
     * @return The public key.
     * Открытый ключ пользователя.
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Set the user's public key.
     * Установить открытый ключ пользователя.
     *
     * @param publicKey The public key to set.
     *                  Открытый ключ пользователя для установки.
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
