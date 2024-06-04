package ru.itcolleg.transaction.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.itcolleg.goal.dto.FinancialGoalDTO;
import ru.itcolleg.goal.service.FinancialGoalService;
import ru.itcolleg.notification.service.NotificationService;
import ru.itcolleg.transaction.dto.TransactionDTO;
import ru.itcolleg.transaction.exception.TransactionNotFoundException;
import ru.itcolleg.transaction.mapper.TransactionMapper;
import ru.itcolleg.transaction.model.Transaction;
import ru.itcolleg.transaction.model.TransactionType;
import ru.itcolleg.transaction.repository.TransactionRepository;
import ru.itcolleg.transaction.repository.TransactionTypeRepository;
import ru.itcolleg.transaction.specifications.TransactionSpecifications;

import java.time.LocalDateTime; // Import LocalDateTime
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final TransactionTypeRepository transactionTypeRepository;
    private final NotificationService notificationService;
    private final FinancialGoalService financialGoalService;
    private final CategoryLimitService categoryLimitService;

    @Autowired
    public TransactionServiceImpl(TransactionMapper transactionMapper, TransactionRepository transactionRepository,
                                  TransactionTypeRepository transactionTypeRepository, NotificationService notificationService,
                                  CategoryLimitService categoryLimitService, FinancialGoalService financialGoalService, CategoryLimitService categoryLimitService1) {
        this.transactionMapper = transactionMapper;
        this.transactionRepository = transactionRepository;
        this.transactionTypeRepository = transactionTypeRepository;
        this.notificationService = notificationService;
        this.financialGoalService = financialGoalService;
        this.categoryLimitService = categoryLimitService1;
    }

    @Override
    public Optional<TransactionDTO> saveTransaction(TransactionDTO transactionDTO, Long userId) {
        logger.info("Начало сохранения транзакции");
        Transaction transaction = transactionMapper.toTransaction(transactionDTO);
        transaction.setUserId(userId);
        Transaction savedTransaction = transactionRepository.save(transaction);
        handleExpenseNotifications(userId, savedTransaction.getTypeId());
        return Optional.of(transactionMapper.toTransactionDTO(savedTransaction));
    }

    private void handleExpenseNotifications(Long userId, Long typeId) {
        if (typeId != null && typeId.equals(findExpensesTypeId())) {
            notificationService.deleteNotifications(userId);
            List<TransactionDTO> expenses = getUserExpenses(userId);
            categoryLimitService.checkLimitsAndSendNotifications(userId, expenses);
        }
    }

    @Override
    public void deleteTransaction(Long userId, Long id) throws TransactionNotFoundException {
        logger.info("Начало удаления транзакции");
        transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Транзакция не найдена для идентификатора " + id));
        transactionRepository.deleteById(id);
    }

    @Override
    public Optional<TransactionDTO> getTransactionById(Long id) throws TransactionNotFoundException {
        logger.info("Начало получения транзакции по идентификатору");
        return transactionRepository.findById(id)
                .map(transactionMapper::toTransactionDTO)
                .map(Optional::of)
                .orElseThrow(() -> new TransactionNotFoundException("Транзакция не найдена для идентификатора " + id));
    }

    @Override
    public Optional<TransactionDTO> updateTransaction(TransactionDTO transactionDTO, Long id) throws TransactionNotFoundException {
        logger.info("Начало обновления транзакции");
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);
        if (transactionOptional.isEmpty()) {
            throw new TransactionNotFoundException("Транзакция не найдена для идентификатора " + id);
        }
        Transaction transaction = transactionOptional.get();
        updateTransactionDetails(transaction, transactionDTO);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return Optional.of(transactionMapper.toTransactionDTO(savedTransaction));
    }

    private void updateTransactionDetails(Transaction transaction, TransactionDTO transactionDTO) {
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setRegular(transactionDTO.getRegular());
        transaction.setCreatedAt(LocalDateTime.parse(transactionDTO.getCreatedAt(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        transaction.setPurpose(transactionDTO.getPurpose());
        transaction.setCategoryId(transactionDTO.getCategoryId());
        transaction.setTypeId(transactionDTO.getTypeId());
    }

    @Override
    public List<TransactionDTO> getFilteredTransactions(Double amount, String purpose, LocalDateTime date, Long categoryId, Long transactionTypeId, Long userId) {
        logger.info("Начало получения всех транзакций с фильтром");
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
            searchQuery = searchQuery.and(TransactionSpecifications.dateGreaterOrEqual(date));
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
    public List<TransactionDTO> getTransactionsByUserId(Long userId) {
        logger.info("Начало получения всех транзакций пользователя");
        if (userId == null) {
            return Collections.emptyList();
        }
        List<Transaction> foundTransactions = transactionRepository.findTransactionsByUserId(userId);
        return foundTransactions.stream()
                .map(transactionMapper::toTransactionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionType> getTransactionTypes() {
        logger.info("Начало получения всех типов транзакций");
        Iterable<TransactionType> transactionTypes = transactionTypeRepository.findAll();
        return iterableToList(transactionTypes);
    }

    @Override
    public List<TransactionDTO> getUserExpenses(Long userId) {
        logger.info("Начало получения всех расходов пользователя");
        return getUserTransactions(userId, findExpensesTypeId());
    }

    private List<TransactionDTO> getUserTransactions(Long userId, Long typeId) {
        if (userId == null || typeId == null) {
            return Collections.emptyList();
        }
        Specification<Transaction> userIdSpec = TransactionSpecifications.hasUserIdEquals(userId);
        Specification<Transaction> typeIdSpec = TransactionSpecifications.hasTransactionTypeEquals(typeId);

        Specification<Transaction> spec = Specification.where(userIdSpec).and(typeIdSpec);

        List<Transaction> foundTransactions = transactionRepository.findAll(spec);

        return foundTransactions.stream()
                .map(transactionMapper::toTransactionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Double calculateUserBalanceAfterSettingGoals(Long userId) {
        logger.info("Начало расчета баланса пользователя после установки целей");
        if (userId == null) {
            return null;
        }
        List<FinancialGoalDTO> userGoals = financialGoalService.getAllUserGoals(userId);

        Long incomesTypeId = findTransactionTypeId("доходы");
        Long expensesTypeId = findTransactionTypeId("расходы");

        List<Transaction> foundUserIncomes = findUserTransactionsByType(userId, incomesTypeId);
        List<Transaction> foundUserExpenses = findUserTransactionsByType(userId, expensesTypeId);

        Set<Transaction> userIncomesAfterGoalStart = filterTransactionsAfterGoalStart(userGoals, foundUserIncomes);
        Set<Transaction> userExpensesAfterGoalStart = filterTransactionsAfterGoalStart(userGoals, foundUserExpenses);

        double totalIncomes = calculateTotalAmount(userIncomesAfterGoalStart);
        double totalExpenses = calculateTotalAmount(userExpensesAfterGoalStart);
        return (totalIncomes - totalExpenses) / userGoals.size();
    }

    @Override
    public Double getUserExpensesAfterSetLimits(Long userId, Long categoryId, Integer startDay) {
        logger.info("Начало получения расходов пользователя после установления лимита");
        if (userId == null) {
            return null;
        }
        Long expensesTypeId = findExpensesTypeId();

        LocalDateTime startDate = getStartingDateForExpensesSearch(startDay);
        LocalDateTime endDate = getEndingDateForExpensesSearch(startDate);

        List<Transaction> foundTransactions = findUserExpensesByCategoryInDateRange(userId, expensesTypeId, categoryId, startDate, endDate);

        return foundTransactions.stream()
                .map(transactionMapper::toTransactionDTO)
                .mapToDouble(TransactionDTO::getAmount)
                .sum();
    }

    private LocalDateTime getStartingDateForExpensesSearch(Integer createdAt) {
        LocalDateTime currentDateTime = LocalDateTime.now(); // LocalDateTime instead of OffsetDateTime
        int currentYear = currentDateTime.getYear();
        int currentMonth = currentDateTime.getMonthValue();
        int startDay = createdAt;
        return LocalDateTime.of(currentYear, currentMonth, startDay, 0, 0);
    }

    private LocalDateTime getEndingDateForExpensesSearch(LocalDateTime startDate) {
        return startDate.plusMonths(1).minusDays(1).withHour(23).withMinute(59).withSecond(59);
    }

    private List<Transaction> findUserExpensesByCategoryInDateRange(Long userId, Long typeId, Long categoryId, LocalDateTime startDate, LocalDateTime endDate) {
        Specification<Transaction> userIdSpec = TransactionSpecifications.hasUserIdEquals(userId);
        Specification<Transaction> typeIdSpec = TransactionSpecifications.hasTransactionTypeEquals(typeId);
        Specification<Transaction> categoryIdSpec = TransactionSpecifications.hasCategoryIdEquals(categoryId);
        Specification<Transaction> dateRangeSpec = TransactionSpecifications.dateGreaterOrEqual(startDate)
                .and(TransactionSpecifications.dateLessOrEqual(endDate));
        Specification<Transaction> spec = Specification.where(userIdSpec).and(typeIdSpec).and(categoryIdSpec).and(dateRangeSpec);
        return transactionRepository.findAll(spec);
    }

    private Long findTransactionTypeId(String type) {
        Optional<TransactionType> typeOptional = this.transactionTypeRepository.findByTypeEquals(type);
        return typeOptional.map(TransactionType::getId).orElse(null);
    }

    private List<Transaction> findUserTransactionsByType(Long userId, Long typeId) {
        Specification<Transaction> userIdSpec = TransactionSpecifications.hasUserIdEquals(userId);
        Specification<Transaction> typeIdSpec = TransactionSpecifications.hasTransactionTypeEquals(typeId);
        Specification<Transaction> spec = Specification.where(userIdSpec).and(typeIdSpec);
        return transactionRepository.findAll(spec);
    }

    public Long findExpensesTypeId() {
        Optional<TransactionType> expensesTypeOptional = transactionTypeRepository.findByTypeEquals("расходы");
        return expensesTypeOptional.map(TransactionType::getId).orElse(null);
    }

    private Set<Transaction> filterTransactionsAfterGoalStart(List<FinancialGoalDTO> userGoals, List<Transaction> transactions) {
        Set<Transaction> filteredTransactions = new HashSet<>();
        for (FinancialGoalDTO financialGoal : userGoals) {
            LocalDateTime goalStartDate = financialGoal.getStartDate(); // LocalDateTime instead of OffsetDateTime
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

    private List<TransactionType> iterableToList(Iterable<TransactionType> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }
}

