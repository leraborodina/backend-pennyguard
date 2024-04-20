package ru.itcolleg.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itcolleg.auth.dto.LoginRequest;
import ru.itcolleg.auth.dto.LoginResponse;
import ru.itcolleg.auth.service.AuthService;
import ru.itcolleg.auth.service.RequiresTokenValidation;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.user.exception.UserLoginCredentialsNotCorrect;
import ru.itcolleg.user.exception.UserNotFoundException;
import ru.itcolleg.user.model.User;
import ru.itcolleg.user.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Optional;

/*
   This annotation is used to indicate that the class is a controller that handles HTTP requests.
   In other words, it's designed for building RESTful web services.

   RESTful stands for Representational State Transfer,
   and it is an architectural style for designing networked applications.

   Key principles of RESTful architecture include:

    1) Statelessness
        Each request from a client to a server must contain all the information needed to understand and process the request.
        The server should not store any information about the client's state between requests.

    2) Client-Server Architecture
        The client and server are separate entities that communicate over a network.
        The client is responsible for the user interface and user experience,
        while the server is responsible for processing requests and managing resources.

    3) Resource-Based
        Resources, which can be data or services, are identified by URIs (Uniform Resource Identifiers).
        Each resource can be manipulated using standard HTTP methods (GET, POST, PUT, DELETE).

    4) Representation
        Resources can have different representations, such as JSON or XML.
        Clients interact with resources by exchanging these representations.

    5) Cacheability
        Responses from the server can be explicitly marked as cacheable or non-cacheable,
        allowing clients to cache responses and improve performance.
 */
@RestController

/*
  This annotation is used to map web requests to specific methods or classes.
  In this case, it specifies that the controller will handle requests that start with "/api/auth".
  For example, if a client sends a request to http://yourdomain.com/api/auth/someEndpoint,
  it will be handled by a method in this controller that is annotated with additional @RequestMapping annotations.
 */

@RequestMapping("/api/auth")
public class AuthRestController {

    private final UserService userService;
    private final AuthService authService;
    private final TokenService tokenService;

    @Autowired
    public AuthRestController(UserService userService, AuthService authService, TokenService tokenService) {
        this.userService = userService;
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("X-UserId") Long userId) {
        try {
            String newAccessToken = tokenService.generateToken(userId);
            userService.updateUserPublicKey(userId, newAccessToken);
            return ResponseEntity.ok(Collections.singletonMap("token", newAccessToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error refreshing token");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Step 1: Authenticate user credentials
            boolean loggedIn = authService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());

            // Step 2: If authentication is successful
            if (loggedIn) {
                // Step 3: Get user information from a database based on the provided email
                Optional<User> optionalUser = userService.getUserByEmail(loginRequest.getEmail());

                // Step 4: If user exists
                if (optionalUser.isPresent()) {
                    // Step 5: Generate a token and update user public key
                    String token = tokenService.generateToken(optionalUser.get().getUserId());
                    userService.updateUserPublicKey(optionalUser.get().getUserId(), tokenService.getEncodedPublicKey());

                    LoginResponse response = new LoginResponse();
                    response.setFirstname(optionalUser.get().getFirstname());
                    response.setLastname(optionalUser.get().getLastname());
                    response.setToken(token);

                    // Step 6: Return response with the token
                    return ResponseEntity.ok(response);
                } else {
                    // Step 7: Handle the case where user information is not available
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User information not available");
                }
            }

            // Step 8: Handle the case where authentication failed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        } catch (UserNotFoundException e) {
            // Step 9: Handle the case where the user is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserLoginCredentialsNotCorrect e) {
            // Step 10: Handle the case where login credentials are incorrect
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Step 11: Handle unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // Step 1: Get all cookies from the request
        Cookie[] cookies = request.getCookies();

        // Step 2: Check if cookies are present
        if (cookies != null) {
            // Step 3: Loop through each cookie
            for (Cookie cookie : cookies) {
                // Step 4: Check if the cookie is the "jwtToken"
                if ("jwtToken".equals(cookie.getName())) {
                    // Step 5: Immediately delete a cookie by setting its maxAge to 0 seconds
                    cookie.setMaxAge(0);
                    // Setting path to "/" means the cookie is valid for the entire domain (http://localhost:8080/)
                    cookie.setPath("/");
                    // The modified cookie is then added to the HTTP response,
                    // ensuring that the client's browser receives and processes the updated cookie, effectively deleting it.
                    // TODO: delete cookie in frontend after this request
                    response.addCookie(cookie);
                    // Step 6: Return a successful response indicating logout
                    return ResponseEntity.ok("Logout successful");
                }
            }
        }

        // Step 7: Return a response indicating the user is not logged in
        return ResponseEntity.ok("User not logged in");
    }
}