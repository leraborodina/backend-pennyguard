package ru.itcolleg.goal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itcolleg.goal.dto.FinancialGoalDTO;
import ru.itcolleg.goal.mapper.FinancialGoalMapper;
import ru.itcolleg.goal.model.FinancialGoal;
import ru.itcolleg.goal.repository.FinancialGoalRepository;
import ru.itcolleg.transaction.model.Transaction;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinancialGoalServiceImpl implements FinancialGoalService {


    private final FinancialGoalRepository repository;


    private final FinancialGoalMapper mapper;

    public FinancialGoalServiceImpl(FinancialGoalRepository repository, FinancialGoalMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<FinancialGoalDTO> getAllGoals(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }

        List<FinancialGoal> foundGoals = repository.findGoalsByUserId(userId);

        return foundGoals.stream().map(goal -> mapper.toDTO(goal)).collect(Collectors.toList());
    }

    @Override
    public FinancialGoalDTO getGoalById(Long id) {
        return mapper.toDTO(repository.findById(id).orElse(null));
    }

    @Override
    public FinancialGoalDTO createGoal(FinancialGoalDTO goalDTO) {
        FinancialGoal financialGoal = mapper.toEntity(goalDTO);

        OffsetDateTime startDate = OffsetDateTime.now();
        OffsetDateTime endDate = startDate.plusMonths(goalDTO.getEndDate());

        financialGoal.setStartDate(startDate);
        financialGoal.setEndDate(endDate);

        return mapper.toDTO(repository.save(financialGoal));
    }

    @Override
    public FinancialGoalDTO updateGoal(Long id, FinancialGoalDTO goalDTO) {
        if (repository.existsById(id)) {
            goalDTO.setId(id);
            return mapper.toDTO(repository.save(mapper.toEntity(goalDTO)));
        }
        return null;
    }

    @Override
    public void deleteGoal(Long id) {
        repository.deleteById(id);
    }
}
