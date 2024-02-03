package ru.itcolleg.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.itcolleg.user.model.User;

import java.util.Optional;

/**
 * Repository interface for interacting with the User entity in the database.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Checks if a user with the given email exists in the database.
     *
     * @param email The email to check.
     * @return true if a user with the email exists, false otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Finds a user by their email.
     *
     * @param email The email to search for.
     * @return An Optional containing the user if found, otherwise empty.
     */
    Optional<User> findByEmail(String email);
}
