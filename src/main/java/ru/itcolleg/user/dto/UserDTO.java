package ru.itcolleg.user.dto;

/**
 * Data Transfer Object (DTO) for representing user information during registration.
 */
public class UserDTO {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String publicKey;

    /**
     * Default constructor.
     */
    public UserDTO() {
    }

    /**
     * Get the user's first name.
     *
     * @return The first name.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Set the user's first name.
     *
     * @param firstname The first name to set.
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Get the user's last name.
     *
     * @return The last name.
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Set the user's last name.
     *
     * @param lastname The last name to set.
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

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

    /**
     * Get the user's public key.
     *
     * @return The public key.
     */
    public String getPublicKey() {
        return publicKey;
    }
}
