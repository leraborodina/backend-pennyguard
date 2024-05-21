package ru.itcolleg.goal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.itcolleg.goal.dto.FinancialGoalDTO;
import ru.itcolleg.goal.exception.GoalNotFoundException;
import ru.itcolleg.goal.mapper.FinancialGoalMapper;
import ru.itcolleg.goal.model.FinancialGoal;
import ru.itcolleg.goal.repository.FinancialGoalRepository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления финансовыми целями.
 * Service for managing financial goals.
 */
@Service
public class FinancialGoalServiceImpl implements FinancialGoalService {

    private static final Logger logger = LoggerFactory.getLogger(FinancialGoalServiceImpl.class);

    private final FinancialGoalRepository financialGoalRepository;
    private final FinancialGoalMapper financialGoalMapper;

    public FinancialGoalServiceImpl(FinancialGoalRepository financialGoalRepository, FinancialGoalMapper financialGoalMapper) {
        this.financialGoalRepository = financialGoalRepository;
        this.financialGoalMapper = financialGoalMapper;
    }

    @Override
    public List<FinancialGoalDTO> getAllUserGoals(Long userId) {
        logger.info("Получение всех финансовых целей для пользователя с ID: {}", userId);

        if (userId == null) {
            logger.warn("Идентификатор пользователя не указан.");
            return Collections.emptyList();
        }

        List<FinancialGoal> foundGoals = financialGoalRepository.findGoalsByUserId(userId);
        return foundGoals.stream().map(financialGoalMapper::toDTO).collect(Collectors.toList());
    }


    @Override
    public FinancialGoalDTO getGoalById(Long id) {
        logger.info("Получение финансовой цели по ID: {}", id);
        FinancialGoal goal = financialGoalRepository.findById(id)
                .orElseThrow(() -> new GoalNotFoundException("Финансовая цель с ID " + id + " не найдена"));

        return financialGoalMapper.toDTO(goal);
    }


    @Override
    public FinancialGoalDTO createGoalForUser(FinancialGoalDTO goalDTO, Long userId) {
        logger.info("Создание новой финансовой цели для пользователя с ID: {}", userId);
        FinancialGoal financialGoal = financialGoalMapper.toEntity(goalDTO);

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusMonths(goalDTO.getEndDate());

        financialGoal.setUserId(userId);
        financialGoal.setStartDate(startDate);
        financialGoal.setEndDate(endDate);

        return financialGoalMapper.toDTO(financialGoalRepository.save(financialGoal));
    }

    @Override
    public FinancialGoalDTO updateGoal(Long id, FinancialGoalDTO goalDTO) {
        logger.info("Обновление финансовой цели с ID: {}", id);

        if (!financialGoalRepository.existsById(id)) {
            throw new GoalNotFoundException("Финансовая цель с ID " + id + " не найдена");
        }

        goalDTO.setId(id);
        FinancialGoal updatedGoal = financialGoalRepository.save(financialGoalMapper.toEntity(goalDTO));
        return financialGoalMapper.toDTO(updatedGoal);
    }

    @Override
    public void deleteGoalById(Long id) {
        logger.info("Удаление финансовой цели по ID: {}", id);
        financialGoalRepository.deleteById(id);
    }
}
