package ru.itcolleg.transaction.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itcolleg.auth.aspect.RequiresTokenValidation;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.exception.TransactionNotFoundException;
import ru.itcolleg.transaction.exception.UnauthorizedTransactionException;
import ru.itcolleg.transaction.model.TransactionType;
import ru.itcolleg.transaction.service.TransactionService;

import java.util.List;
import java.util.Optional;

/**
 * REST контроллер для управления транзакциями.
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionRestController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionRestController.class);

    private final TransactionService transactionService;
    private final TokenService tokenService;

    @Autowired
    public TransactionRestController(TransactionService transactionService, TokenService tokenService) {
        this.transactionService = transactionService;
        this.tokenService = tokenService;
    }

    /**
     * Создает новую транзакцию.
     *
     * @param transactionDTO Данные новой транзакции.
     * @param token          Токен авторизации.
     * @return ResponseEntity с созданной транзакцией, если успешно, в противном случае возвращает статус ошибки.
     */
    @RequiresTokenValidation
    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transactionDTO, @RequestHeader("Authorization") String token) {
        try {
            Long userId = tokenService.extractUserIdFromToken(token);
            Optional<TransactionDTO> savedTransaction = transactionService.saveTransaction(transactionDTO, userId);
            return savedTransaction.map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto))
                    .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } catch (Exception e) {
            logger.error("Error occurred while creating transaction", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Получает все транзакции пользователя.
     *
     * @param token Токен авторизации.
     * @return ResponseEntity с списком транзакций пользователя, если успешно, в противном случае возвращает статус ошибки.
     */
    @RequiresTokenValidation
    @GetMapping("/user/")
    public ResponseEntity<?> getTransactionsByUserId(@RequestHeader("Authorization") String token) {
        try {
            Long userId = tokenService.extractUserIdFromToken(token);
            List<TransactionDTO> userTransactions = transactionService.getTransactionsByUserId(userId);
            return ResponseEntity.ok(userTransactions);
        } catch (Exception e) {
            logger.error("Error occurred while getting transactions by user ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Получает расходы пользователя.
     *
     * @param token Токен авторизации.
     * @return ResponseEntity с расходами пользователя, если успешно, в противном случае возвращает статус ошибки.
     */
    @RequiresTokenValidation
    @GetMapping("/user/expences")
    public ResponseEntity<?> getExpencesByUserId(@RequestHeader("Authorization") String token) {
        try {
            Long userId = tokenService.extractUserIdFromToken(token);
            List<TransactionDTO> userExpences = transactionService.getUserExpenses(userId);
            return ResponseEntity.ok(userExpences);
        } catch (Exception e) {
            logger.error("Error occurred while getting user expenses", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Получает транзакцию по ее ID.
     *
     * @param id    ID транзакции.
     * @param token Токен авторизации.
     * @return ResponseEntity с транзакцией, если найдена, в противном случае возвращает 404 Not Found.
     */
    @RequiresTokenValidation
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            Optional<TransactionDTO> receivedTransaction = transactionService.getTransactionById(id);
            return receivedTransaction.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error occurred while getting transaction by ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Получает типы транзакций.
     *
     * @return ResponseEntity с типами транзакций, если успешно, в противном случае возвращает статус ошибки.
     */
    @GetMapping("/transactionTypes")
    public ResponseEntity<?> getTransactionTypes() {
        try {
            List<TransactionType> transactionTypes = transactionService.getTransactionTypes();
            return new ResponseEntity<>(transactionTypes, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while getting transaction types", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Обновляет существующую транзакцию.
     *
     * @param transactionDTO Данные обновленной транзакции.
     * @param id             ID транзакции для обновления.
     * @return ResponseEntity с обновленной транзакцией, если успешно, в противном случае возвращает 404 Not Found.
     */
    @RequiresTokenValidation
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody TransactionDTO transactionDTO, @PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            Long userId = tokenService.extractUserIdFromToken(token);

            Optional<TransactionDTO> updatedTransaction = transactionService.updateTransaction(transactionDTO, id, userId);
            return updatedTransaction.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error occurred while updating transaction", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Удаляет транзакцию по ее ID.
     *
     * @param id    ID транзакции.
     * @param token Токен авторизации.
     * @return ResponseEntity без содержимого, если удаление прошло успешно, в противном случае возвращает статус ошибки.
     */
    @RequiresTokenValidation
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            Long userId = tokenService.extractUserIdFromToken(token);
            transactionService.deleteTransaction(userId, id);
            return ResponseEntity.ok().build();
        } catch (TransactionNotFoundException e) {
            logger.error("Error occurred while deleting transaction", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error occurred while deleting transaction", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Получает доходы пользователя.
     *
     * @param token Токен авторизации.
     * @return ResponseEntity с доходами пользователя, если успешно, в противном случае возвращает статус ошибки.
     */
    @GetMapping("/user/incomes")
    public ResponseEntity<?> getUserIncomes(@RequestHeader("Authorization") String token) {
        try {
            Long userId = tokenService.extractUserIdFromToken(token);
            Double userIncomes = transactionService.calculateUserBalanceAfterSettingGoals(userId);
            return ResponseEntity.ok(userIncomes);
        } catch (Exception e) {
            logger.error("Error occurred while getting user incomes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
