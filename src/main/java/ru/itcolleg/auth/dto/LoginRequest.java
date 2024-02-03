package ru.itcolleg.auth.dto;

/**
 * Represents the request containing user login credentials.
 */
public class LoginRequest {

    private String email;
    private String password;

    /**
     * Get the user's email address.
     *
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the user's email address.
     *
     * @param email The email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the user's password.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the user's password.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
