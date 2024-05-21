package ru.itcolleg.user.repository;

import org.springframework.data.repository.CrudRepository;
import ru.itcolleg.user.model.User;

import java.util.Optional;

/**
 * Repository interface for interacting with the User entity in the database.
 * Интерфейс репозитория для взаимодействия с сущностью пользователя в базе данных.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Checks if a user with the given email exists in the database.
     * Проверяет, существует ли пользователь с указанным адресом электронной почты в базе данных.
     *
     * @param email The email to check.
     *              Адрес электронной почты для проверки.
     * @return true if a user with the email exists, false otherwise.
     * true, если пользователь с таким адресом электронной почты существует, в противном случае - false.
     */
    boolean existsByEmail(String email);

    /**
     * Finds a user by their email.
     * Находит пользователя по его адресу электронной почты.
     *
     * @param email The email to search for.
     *              Адрес электронной почты для поиска.
     * @return An Optional containing the user if found, otherwise empty.
     * Optional, содержащий пользователя, если он найден, в противном случае - пустой.
     */
    Optional<User> findByEmail(String email);
}
