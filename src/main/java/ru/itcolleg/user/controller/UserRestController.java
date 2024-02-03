package ru.itcolleg.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itcolleg.user.dto.UserDTO;
import ru.itcolleg.auth.dto.LoginResponse;
import ru.itcolleg.user.exception.UserAlreadyExistsException;
import ru.itcolleg.user.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     *
     * @param userDTO The user information to be registered.
     * @return ResponseEntity containing the saved user information or an error response.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            // Attempt to save the user
            Optional<LoginResponse> savedUser = userService.saveUser(userDTO);

            // If successful, return the saved user with HTTP status 201 (Created)
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            // If user already exists, return a BAD REQUEST response with the error message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // If an unexpected exception occurs, return an INTERNAL SERVER ERROR response with the error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
