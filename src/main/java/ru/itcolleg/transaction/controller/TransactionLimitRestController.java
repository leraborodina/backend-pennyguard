package ru.itcolleg.transaction.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.transaction.dto.TransactionLimitDTO;
import ru.itcolleg.transaction.service.TransactionLimitService;
@RestController
@RequestMapping("/api/transaction-limits")
public class TransactionLimitRestController {

    private final TransactionLimitService transactionLimitService;
    private final TokenService tokenService;

    public TransactionLimitRestController(TransactionLimitService transactionLimitService, TokenService tokenService) {
        this.transactionLimitService = transactionLimitService;
        this.tokenService = tokenService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionLimitById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        if (!tokenService.validateJwtToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to access transaction limits for this user");
        }
        try {
            TransactionLimitDTO limitDTO = transactionLimitService.getTransactionLimitById(id);
            return ResponseEntity.ok(limitDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> setTransactionLimit(@RequestBody TransactionLimitDTO limitDTO, @RequestHeader("Authorization") String token) {
        if (!tokenService.validateJwtToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to set transaction limits for this user");
        }
        try {
            Long extractedUserId = tokenService.extractUserIdFromToken(token);
            transactionLimitService.setTransactionLimit(limitDTO, extractedUserId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransactionLimit(@PathVariable Long id, @RequestBody TransactionLimitDTO limitDTO, @RequestHeader("Authorization") String token) {
        if (!tokenService.validateJwtToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update transaction limits for this user");
        }
        try {
            if (!id.equals(limitDTO.getId())) {
                return ResponseEntity.badRequest().body("ID in path does not match ID in request body");
            }
            transactionLimitService.updateTransactionLimit(limitDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransactionLimit(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        if (!tokenService.validateJwtToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete transaction limits for this user");
        }
        try {
            transactionLimitService.deleteTransactionLimit(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
