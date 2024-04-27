package ru.itcolleg.transaction.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itcolleg.auth.service.RequiresTokenValidation;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.transaction.dto.TransactionLimitDTO;
import ru.itcolleg.transaction.model.TransactionLimitType;
import ru.itcolleg.transaction.service.TransactionLimitService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/transaction-limits")
public class TransactionLimitRestController {

    private final TransactionLimitService transactionLimitService;
    private final TokenService tokenService;

    public TransactionLimitRestController(TransactionLimitService transactionLimitService, TokenService tokenService) {
        this.transactionLimitService = transactionLimitService;
        this.tokenService = tokenService;
    }

    @GetMapping
    public ResponseEntity<?> getTransactionLimitsByUserId(@RequestHeader("Authorization") String token) {
        try {
            Long userId = tokenService.extractUserIdFromToken(token);
            List<TransactionLimitDTO> transactionLimits = transactionLimitService.getTransactionLimitsByUserId(userId);
            return ResponseEntity.ok(transactionLimits);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database Error");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @RequiresTokenValidation
    @GetMapping("/{id}")
    public ResponseEntity<TransactionLimitDTO> getTransactionLimitById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            TransactionLimitDTO limitDTO = transactionLimitService.getTransactionLimitById(id);
            return ResponseEntity.ok(limitDTO);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @RequiresTokenValidation
    @PostMapping
    public ResponseEntity<Void> createTransactionLimit(@RequestBody TransactionLimitDTO limitDTO, @RequestHeader("Authorization") String token) {
        try {
            Long extractedUserId = tokenService.extractUserIdFromToken(token);
            transactionLimitService.setTransactionLimit(limitDTO, extractedUserId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequiresTokenValidation
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTransactionLimit(@PathVariable Long id, @RequestBody TransactionLimitDTO limitDTO, @RequestHeader("Authorization") String token) {
        try {
            if (!id.equals(limitDTO.getId())) {
                return ResponseEntity.badRequest().build();
            }
            transactionLimitService.updateTransactionLimit(limitDTO);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequiresTokenValidation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionLimit(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            transactionLimitService.deleteTransactionLimit(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/limit-types")
    public ResponseEntity<List<TransactionLimitType>> getLimitTypes() {
        try {
            List<TransactionLimitType> limitTypes = transactionLimitService.getLimitTypes();
            return ResponseEntity.ok(limitTypes);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

