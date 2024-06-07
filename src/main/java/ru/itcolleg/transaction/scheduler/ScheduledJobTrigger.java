package ru.itcolleg.transaction.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import ru.itcolleg.transaction.model.Transaction;
import ru.itcolleg.transaction.repository.TransactionRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Компонент, отвечающий за триггер запланированной работы.
 */
@Component
public class ScheduledJobTrigger {

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Запускает работу сразу после запуска приложения.
     */
    @Scheduled(cron = "0 */30 * * * *") // каждые 30 минут для тестирования
    // @Scheduled(cron = "0 0 0 1 * *") // каждый месяц в полночь первого числа
    public void triggerJob() {
        taskScheduler.schedule(this::executeJob, Instant.now());
    }

    /**
     * Метод для выполнения логики работы.
     */
    private void executeJob() {
        List<Transaction> regularTransactions = transactionRepository.findByRegularTrue();
        processRecurringTransactions(regularTransactions);
    }

    /**
     * Обрабатывает повторяющиеся транзакции.
     *
     * @param regularTransactions список регулярных транзакций
     */
    private void processRecurringTransactions(List<Transaction> regularTransactions) {
        // Генерирует повторяющиеся транзакции для каждой регулярной транзакции
        for (Transaction regularTransaction : regularTransactions) {
            // Рассчитывает дату следующего события
            LocalDateTime nextOccurrence = calculateNextOccurrence(regularTransaction.getCreatedAt());

            // Создает новую транзакцию с теми же данными и датой следующего события
            Transaction recurringTransaction = new Transaction();
            recurringTransaction.setCategoryId(regularTransaction.getCategoryId());
            recurringTransaction.setRegular(regularTransaction.getRegular());
            recurringTransaction.setUserId(regularTransaction.getUserId());
            recurringTransaction.setAmount(regularTransaction.getAmount());
            recurringTransaction.setPurpose(regularTransaction.getPurpose());
            recurringTransaction.setTypeId(regularTransaction.getTypeId()); // Сохраняет оригинальный тип
            recurringTransaction.setCreatedAt(nextOccurrence);

            // Сохраняет повторяющуюся транзакцию
            transactionRepository.save(recurringTransaction);
        }
    }

    /**
     * Рассчитывает дату следующего события на основе даты транзакции.
     *
     * @param transactionDate дата транзакции
     * @return дата следующего события
     */
    private LocalDateTime calculateNextOccurrence(LocalDateTime transactionDate) {
        // Получает текущую дату
        LocalDateTime currentDate = LocalDateTime.now();

        // Проверяет, если дата транзакции в будущем
        if (transactionDate.isAfter(currentDate)) {
            return transactionDate;
        }

        // Рассчитывает следующее событие путем добавления одного месяца к дате транзакции
        return transactionDate.plusMonths(1);
    }

    /**
     * Конфигурирует TaskScheduler.
     *
     * @return TaskScheduler
     */
    @Bean
    public TaskScheduler taskScheduler() {
        return new ThreadPoolTaskScheduler();
    }
}
