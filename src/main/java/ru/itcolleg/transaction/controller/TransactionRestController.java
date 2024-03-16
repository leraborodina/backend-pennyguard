package ru.itcolleg.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.model.Category;
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

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        try {
            Optional<TransactionDTO> savedTransaction = transactionService.saveTransaction(transactionDTO);
            return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/user/")
    public ResponseEntity<?> getTransactionsByUserId(@RequestHeader("Authorization") String token) {
        // Authorization check
        if (!tokenService.validateJwtToken(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to access transactions for this user");
        }

        // If authorized, fetch transactions
        try {
            // Extract user ID from the token
            Long extractedUserId = tokenService.extractUserIdFromToken(token);
            List<TransactionDTO> userTransactions = transactionService.getTransactionsByUserId(extractedUserId);
            return new ResponseEntity<>(userTransactions, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Optional<TransactionDTO> receivedTransaction = transactionService.getTransactionById(id);
            return new ResponseEntity<>(receivedTransaction, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        try {
            List<Category> categories = transactionService.getCategories();
            return new ResponseEntity<>(categories, HttpStatus.OK);
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

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody TransactionDTO transactionDTO, @PathVariable Long id) {
        try {
            Optional<TransactionDTO> updatedTransaction = transactionService.updateTransaction(transactionDTO, id);
            return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(required = false) Double amount, @RequestParam(required = false) String purpose, @RequestParam(required = false) String date, @RequestParam(required = false) String category) {
        try {
            List<TransactionDTO> transactions = transactionService.getAll(amount, purpose, LocalDate.parse(date), category);
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // TODO: create method delete transaction by id

}
