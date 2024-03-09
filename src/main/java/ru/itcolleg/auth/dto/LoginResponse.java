package ru.itcolleg.auth.dto;

/**
 * Represents the response containing user information along with an authentication token.
 */
public class LoginResponse {

    private String firstname;
    private String lastname;
    private String token;

    /**
     * Default constructor.
     */
    public LoginResponse() {
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
     * Get the authentication token.
     *
     * @return The authentication token.
     */
    public String getToken() {
        return token;
    }

    /**
     * Set the authentication token.
     *
     * @param token The authentication token to set.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Override the toString() method to provide a string representation of the object.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "LoginResponse{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
