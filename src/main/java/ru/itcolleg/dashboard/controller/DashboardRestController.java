package ru.itcolleg.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.user.service.UserService;

@RestController
@RequestMapping("/api")
public class DashboardRestController {

    private final TokenService tokenService;
    private final UserService userService;

    @Autowired
    public DashboardRestController(TokenService tokenService, UserService userService){
        this.tokenService = tokenService;
        this.userService = userService;
    }

    /**
     * Retrieves dashboard content for the authenticated user.
     *
     * @param authorizationHeader The Authorization header containing the bearer token.
     * @param userEmail The user's email extracted from the X-User-Email header.
     * @return ResponseEntity containing the dashboard content or an unauthorized response.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard(@RequestHeader("Authorization") String authorizationHeader,
                                               @RequestHeader("X-User-Email") String userEmail) {
        try {
            // Extract the token from the Authorization header
            String token = authorizationHeader.replaceFirst("Bearer", "").trim();

            // Retrieve the user's public key using the email
            String publicKeyString = userService.getPublicKeyByEmail(userEmail);

            // Verify the token using the public key
            if (tokenService.verifyToken(token, publicKeyString)) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

                String dummyResponse = "{\"message\": \"Hello, this is a dummy response!\"}";

                // If verification is successful, return the dashboard content
                return new ResponseEntity<>(dummyResponse, headers, HttpStatus.OK);
            } else {
                // If verification fails, return an unauthorized response
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
        } catch (Exception e) {
            // If an exception occurs, return an unauthorized response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }
}
