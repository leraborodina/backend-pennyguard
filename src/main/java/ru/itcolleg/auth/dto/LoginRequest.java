package ru.itcolleg.auth.dto;

/**
 * Represents the request containing user login credentials.
 * Представляет запрос, содержащий учетные данные пользователя.
 */
public class LoginRequest {

    private String email;
    private String password;

    /**
     * Get the user's email address.
     * Получить адрес электронной почты пользователя.
     *
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the user's email address.
     * Установить адрес электронной почты пользователя.
     *
     * @param email The email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the user's password.
     * Получить пароль пользователя.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the user's password.
     * Установить пароль пользователя.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
