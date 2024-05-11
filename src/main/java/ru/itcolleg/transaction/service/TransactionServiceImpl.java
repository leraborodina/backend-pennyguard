package ru.itcolleg.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.itcolleg.goal.dto.FinancialGoalDTO;
import ru.itcolleg.goal.service.FinancialGoalService;
import ru.itcolleg.notification.service.NotificationService;
import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.exception.TransactionNotFoundException;
import ru.itcolleg.transaction.exception.UnauthorizedTransactionException;
import ru.itcolleg.transaction.mapper.TransactionMapper;
import ru.itcolleg.transaction.model.Transaction;
import ru.itcolleg.transaction.model.TransactionType;
import ru.itcolleg.transaction.repository.TransactionRepository;
import ru.itcolleg.transaction.repository.TransactionTypeRepository;
import ru.itcolleg.transaction.specifications.TransactionSpecifications;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final TransactionTypeRepository transactionTypeRepository;
    private final NotificationService notificationService;
    private final TransactionLimitService transactionLimitService;
    private final FinancialGoalService financialGoalService;

    @Autowired
    public TransactionServiceImpl(TransactionMapper transactionMapper, TransactionRepository transactionRepository, TransactionTypeRepository transactionTypeRepository, NotificationService notificationService, TransactionLimitService transactionLimitService, FinancialGoalService financialGoalService) {
        this.transactionMapper = transactionMapper;
        this.transactionRepository = transactionRepository;
        this.transactionTypeRepository = transactionTypeRepository;
        this.notificationService = notificationService;
        this.transactionLimitService = transactionLimitService;
        this.financialGoalService = financialGoalService;
    }

    @Override
    public Optional<TransactionDTO> saveTransaction(TransactionDTO transactionDTO, Long userId) {
        Transaction transaction = transactionMapper.toTransaction(transactionDTO);
        transaction.setUserId(userId);
        Transaction savedTransaction = transactionRepository.save(transaction);

        Optional<TransactionType> typeOptional = this.transactionTypeRepository.findById(transaction.getTypeId());

        if (typeOptional.isPresent()) {
            TransactionType transactionType = typeOptional.get();

            if (transactionType.getType().equalsIgnoreCase("expences")) {
                notificationService.deleteNotifications(userId);
                List<TransactionDTO> expenses = this.getUserExpencesByPeriodAndCategory(userId, transactionType.getId());
                this.transactionLimitService.checkLimitsAndSendNotifications(userId, expenses);
            }
        }
        return Optional.of(transactionMapper.toTransactionDTO(savedTransaction));
    }

    @Override
    public void deleteTransaction(Long userId, Long id) throws TransactionNotFoundException, UnauthorizedTransactionException {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found for id " + id));

        if (!transaction.getUserId().equals(userId)) {
            throw new UnauthorizedTransactionException("UserIds don't match");
        }

        transactionRepository.deleteById(id);
    }

    @Override
    public Optional<TransactionDTO> getTransactionById(Long id) throws TransactionNotFoundException {
        return transactionRepository.findById(id)
                .map(transactionMapper::toTransactionDTO)
                .map(Optional::of)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found for id " + id));
    }

    @Override
    public Optional<TransactionDTO> updateTransaction(TransactionDTO transactionDTO, Long id) throws TransactionNotFoundException {
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);
        if (transactionOptional.isEmpty()) {
            throw new TransactionNotFoundException("Transaction not found for id " + id);
        }

        Transaction transaction = transactionOptional.get();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setRegular(transactionDTO.getRegular());
        transaction.setCreatedAt(transactionDTO.getCreatedAt());
        transaction.setPurpose(transactionDTO.getPurpose());
        transaction.setCategoryId(transactionDTO.getCategoryId());
        transaction.setTypeId(transactionDTO.getTypeId());

        Transaction savedTransaction = transactionRepository.save(transaction);

        TransactionDTO dto = transactionMapper.toTransactionDTO(savedTransaction);
        return Optional.of(dto);
    }

    @Override
    public List<TransactionDTO> getTransactionsByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }

        List<Transaction> foundTransactions = transactionRepository.findTransactionsByUserId(userId);

        return foundTransactions.stream()
                .map(transactionMapper::toTransactionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> getUserExpencesByPeriodAndCategory(Long userId, Long typeId) {
        if (userId == null || typeId == null) {
            return null;
        }

        // Define userId, typeId, and categoryId specifications
        Specification<Transaction> userIdSpec = TransactionSpecifications.hasUserIdEquals(userId);
        Specification<Transaction> typeIdSpec = TransactionSpecifications.hasTransactionTypeEquals(typeId);

        // Combine specifications
        Specification<Transaction> spec = Specification.where(userIdSpec)
                .and(typeIdSpec);

        // Retrieve transactions matching the specification
        List<Transaction> foundTransactions = transactionRepository.findAll(spec);

        // Map found transactions to DTOs
        return foundTransactions.stream()
                .map(transactionMapper::toTransactionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> getUserExpences(Long userId) {
        if (userId == null) {
            return null;
        }

        Optional<TransactionType> typeOptional = this.transactionTypeRepository.findByTypeEquals("expences");

        if (typeOptional.isEmpty()) {
            return Collections.emptyList();
        }

        TransactionType transactionType = typeOptional.get();

        // Define userId and typeId
        Specification<Transaction> userIdSpec = TransactionSpecifications.hasUserIdEquals(userId);
        Specification<Transaction> typeIdSpec = TransactionSpecifications.hasTransactionTypeEquals(transactionType.getId());

        // Combine specifications
        Specification<Transaction> spec = Specification.where(userIdSpec)
                .and(typeIdSpec);

        // Retrieve transactions matching the specification
        List<Transaction> foundTransactions = transactionRepository.findAll(spec);

        // Map found transactions to DTOs
        return foundTransactions.stream()
                .map(transactionMapper::toTransactionDTO)
                .collect(Collectors.toList());
    }


    @Override
    public Double calculateUserBalanceAfterSettingGoals(Long userId) {
        if (userId == null) {
            return null;
        }

        // Retrieve all financial goals for the user
        List<FinancialGoalDTO> userGoals = financialGoalService.getAllGoals(userId);

        // Retrieve transaction types for incomes and expenses
        Optional<TransactionType> incomesTypeOptional = transactionTypeRepository.findByTypeEquals("incomes");
        Optional<TransactionType> expensesTypeOptional = transactionTypeRepository.findByTypeEquals("expenses");

        Long incomesTypeId = incomesTypeOptional.map(TransactionType::getId).orElse(null);
        Long expensesTypeId = expensesTypeOptional.map(TransactionType::getId).orElse(null);

        // Define specifications for filtering transactions
        Specification<Transaction> userIdSpec = TransactionSpecifications.hasUserIdEquals(userId);
        Specification<Transaction> incomesSpec = TransactionSpecifications.hasTransactionTypeEquals(incomesTypeId);
        Specification<Transaction> expensesSpec = TransactionSpecifications.hasTransactionTypeEquals(expensesTypeId);

        // Combine specifications for incomes and expenses
        Specification<Transaction> incomesByUserIdSpec = Specification.where(userIdSpec).and(incomesSpec);
        Specification<Transaction> expensesByUserIdSpec = Specification.where(userIdSpec).and(expensesSpec);

        // Retrieve transactions matching the specifications
        List<Transaction> foundUserIncomes = transactionRepository.findAll(incomesByUserIdSpec);
        List<Transaction> foundUserExpenses = transactionRepository.findAll(expensesByUserIdSpec);

        // Filter incomes and expenses occurred after the start date of each financial goal
        Set<Transaction> userIncomesAfterGoalStart = filterTransactionsAfterGoalStart(userGoals, foundUserIncomes);
        Set<Transaction> userExpensesAfterGoalStart = filterTransactionsAfterGoalStart(userGoals, foundUserExpenses);

        // Calculate total income and total expenses
        double totalIncomes = calculateTotalAmount(userIncomesAfterGoalStart);
        double totalExpenses = calculateTotalAmount(userExpensesAfterGoalStart);

        // Calculate and return user balance after set goals
        return (totalIncomes - totalExpenses) / userGoals.size();
    }

    private Set<Transaction> filterTransactionsAfterGoalStart(List<FinancialGoalDTO> userGoals, List<Transaction> transactions) {
        Set<Transaction> filteredTransactions = new HashSet<>();
        for (FinancialGoalDTO financialGoal : userGoals) {
            OffsetDateTime goalStartDate = financialGoal.getStartDate();
            for (Transaction transaction : transactions) {
                if (transaction.getCreatedAt().isAfter(goalStartDate)) {
                    filteredTransactions.add(transaction);
                }
            }
        }
        return filteredTransactions;
    }

    private double calculateTotalAmount(Set<Transaction> transactions) {
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }


    @Override
    public Double getUserFixExpenses(Long userId, Long categoryId, Integer salaryDay) {
        if (userId == null) {
            return null;
        }

        Optional<TransactionType> expensesType = transactionTypeRepository.findByTypeEquals("expences");

        if (expensesType.isEmpty()) {
            return 0.0; // No expenses type found, return 0.0
        }

        Long typeId = expensesType.get().getId();

        // Get current year and month
        OffsetDateTime currentDateTime = OffsetDateTime.now();
        int currentYear = currentDateTime.getYear();
        int currentMonth = currentDateTime.getMonthValue();

        // Get salary day from user limits
        int startDay = salaryDay;

        // Set start date to the salary day of the current month
        OffsetDateTime startDate = OffsetDateTime.of(currentYear, currentMonth, startDay, 0, 0, 0, 0, ZoneOffset.UTC);

        // Set end date to the last day of the current month
        OffsetDateTime endDate = startDate.plusMonths(1).minusDays(1).withHour(23).withMinute(59).withSecond(59);

        // Define userId, typeId, and categoryId specifications
        Specification<Transaction> userIdSpec = TransactionSpecifications.hasUserIdEquals(userId);
        Specification<Transaction> typeIdSpec = TransactionSpecifications.hasTransactionTypeEquals(typeId);
        Specification<Transaction> categoryIdSpec = TransactionSpecifications.hasCategoryIdEquals(categoryId);

        // Define date range specification
        Specification<Transaction> dateRangeSpec = TransactionSpecifications.dateGreaterOrEqual(startDate)
                .and(TransactionSpecifications.dateLessOrEqual(endDate));

        // Combine specifications
        Specification<Transaction> spec = Specification.where(userIdSpec)
                .and(typeIdSpec)
                .and(categoryIdSpec)
                .and(dateRangeSpec);

        // Retrieve transactions matching the specification
        List<Transaction> foundTransactions = transactionRepository.findAll(spec);

        // Map found transactions to DTOs and calculate the sum of amounts
        return foundTransactions.stream()
                .map(transactionMapper::toTransactionDTO)
                .mapToDouble(TransactionDTO::getAmount)
                .sum();
    }

    @Override
    public List<TransactionDTO> getAll(Double amount, String purpose, LocalDate date, Long categoryId, Long transactionTypeId, Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }

        Specification<Transaction> searchQuery = Specification.where(TransactionSpecifications.hasUserIdEquals(userId));

        if (amount != null && amount > 0) {
            searchQuery = searchQuery.and(TransactionSpecifications.amountGreaterOrEqual(amount));
        }

        if (purpose != null && !purpose.isEmpty()) {
            searchQuery = searchQuery.and(TransactionSpecifications.hasPurposeLike(purpose));
        }

        if (date != null) {
            searchQuery = searchQuery.and(TransactionSpecifications.dateGreaterOrEqual(OffsetDateTime.from(date)));
        }

        if (categoryId != null) {
            searchQuery = searchQuery.and(TransactionSpecifications.hasCategoryIdEquals(categoryId));
        }

        if (transactionTypeId != null) {
            searchQuery = searchQuery.and(TransactionSpecifications.hasTransactionTypeEquals(transactionTypeId));
        }

        List<Transaction> foundTransactions = transactionRepository.findAll(searchQuery);

        return foundTransactions.stream()
                .map(transactionMapper::toTransactionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionType> getTransactionTypes() {
        Iterable<TransactionType> transactionTypes = transactionTypeRepository.findAll();
        return iterableToList(transactionTypes);
    }

    private <T> List<T> iterableToList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }
}