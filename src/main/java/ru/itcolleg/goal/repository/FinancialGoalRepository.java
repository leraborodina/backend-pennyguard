package ru.itcolleg.goal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itcolleg.goal.model.FinancialGoal;

import java.util.List;

public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, Long> {
    List<FinancialGoal> findGoalsByUserId(Long userId);
}
