package ru.itcolleg.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itcolleg.auth.service.RequiresTokenValidation;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.exception.TransactionNotFoundException;
import ru.itcolleg.transaction.exception.UnauthorizedTransactionException;
import ru.itcolleg.transaction.model.Category;
import ru.itcolleg.transaction.model.TransactionLimitType;
import ru.itcolleg.transaction.model.TransactionType;
import ru.itcolleg.transaction.service.TransactionService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionRestController {
    private final TransactionService transactionService;

    private final TokenService tokenService;

    @Autowired
    public TransactionRestController(TransactionService transactionService, TokenService tokenService) {
        this.transactionService = transactionService;
        this.tokenService = tokenService;
    }

    @RequiresTokenValidation
    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transactionDTO, @RequestHeader("Authorization") String token) {
        try {
            Long userId = tokenService.extractUserIdFromToken(token);
            Optional<TransactionDTO> savedTransaction = transactionService.saveTransaction(transactionDTO, userId);
            return savedTransaction.map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto))
                    .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequiresTokenValidation
    @GetMapping("/user/")
    public ResponseEntity<?> getTransactionsByUserId(@RequestHeader("Authorization") String token) {
        try {
            Long userId = tokenService.extractUserIdFromToken(token);
            List<TransactionDTO> userTransactions = transactionService.getTransactionsByUserId(userId);
            return ResponseEntity.ok(userTransactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequiresTokenValidation
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            Optional<TransactionDTO> receivedTransaction = transactionService.getTransactionById(id);
            return receivedTransaction.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/transactionTypes")
    public ResponseEntity<?> getTransactionTypes() {
        try {
            List<TransactionType> transactionTypes = transactionService.getTransactionTypes();
            return new ResponseEntity<>(transactionTypes, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequiresTokenValidation
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody TransactionDTO transactionDTO, @PathVariable Long id) {
        try {
            Optional<TransactionDTO> updatedTransaction = transactionService.updateTransaction(transactionDTO, id);
            return updatedTransaction.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequiresTokenValidation
    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(required = false) Double amount, @RequestParam(required = false) String purpose, @RequestParam(required = false) String date, @RequestParam(required = false) Long categoryId, @RequestParam(required = false) Long transactionTypeId, @RequestParam(required = false) Long userId) {
        try {
            List<TransactionDTO> transactions = transactionService.getAll(amount, purpose, LocalDate.parse(date), categoryId, transactionTypeId, userId);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @RequiresTokenValidation
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            Long userId = tokenService.extractUserIdFromToken(token);
            transactionService.deleteTransaction(userId, id);
            return ResponseEntity.ok().build();
        } catch (TransactionNotFoundException | UnauthorizedTransactionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
