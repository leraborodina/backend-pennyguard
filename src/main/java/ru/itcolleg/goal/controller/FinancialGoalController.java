package ru.itcolleg.goal.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itcolleg.auth.service.TokenService;
import ru.itcolleg.goal.dto.FinancialGoalDTO;
import ru.itcolleg.goal.mapper.FinancialGoalMapper;
import ru.itcolleg.goal.service.FinancialGoalService;
import ru.itcolleg.transaction.dto.TransactionDTO;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class FinancialGoalController {
    @Autowired
    private FinancialGoalService service;

    @Autowired
    private FinancialGoalMapper mapper;

    private final TokenService tokenService;

    public FinancialGoalController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping
    public ResponseEntity<?> getAllGoals(@RequestHeader("Authorization") String token) {
        try {
            Long userId = tokenService.extractUserIdFromToken(token);
            List<FinancialGoalDTO> userGoals = service.getAllGoals(userId);
            return ResponseEntity.ok(userGoals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialGoalDTO> getGoalById(@PathVariable Long id) {
        FinancialGoalDTO goalDTO = service.getGoalById(id);
        return goalDTO != null ? ResponseEntity.ok(goalDTO) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<FinancialGoalDTO> createGoal(@RequestBody FinancialGoalDTO goalDTO, @RequestHeader("Authorization") String token) {
        Long userId = tokenService.extractUserIdFromToken(token);
        goalDTO.setUserId(userId);
        FinancialGoalDTO createdGoalDTO = service.createGoal(goalDTO);
        return new ResponseEntity<>(createdGoalDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinancialGoalDTO> updateGoal(@PathVariable Long id, @RequestBody FinancialGoalDTO goalDTO) {
        FinancialGoalDTO updatedGoalDTO = service.updateGoal(id, goalDTO);
        return updatedGoalDTO != null ? ResponseEntity.ok(updatedGoalDTO) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        service.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }
}
