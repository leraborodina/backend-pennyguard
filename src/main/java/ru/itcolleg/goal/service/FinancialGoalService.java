package ru.itcolleg.goal.service;

import ru.itcolleg.goal.dto.FinancialGoalDTO;
import ru.itcolleg.goal.model.FinancialGoal;

import java.util.List;

public interface FinancialGoalService {
    List<FinancialGoalDTO> getAllGoals(Long userId);
    FinancialGoalDTO getGoalById(Long id);
    FinancialGoalDTO createGoal(FinancialGoalDTO goalDTO);
    FinancialGoalDTO updateGoal(Long id, FinancialGoalDTO goalDTO);
    void deleteGoal(Long id);
}
