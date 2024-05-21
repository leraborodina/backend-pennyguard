package ru.itcolleg.goal.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime; // Change OffsetDateTime to LocalDateTime

/**
 * Сущность финансовой цели.
 * Entity class for a financial goal.
 */
@Entity
@Table(name = "[financial_goal]", schema = "[dbo]")
public class FinancialGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal sum;

    @Column(name = "start_date")
    private LocalDateTime startDate; // Change OffsetDateTime to LocalDateTime

    @Column(name = "end_date")
    private LocalDateTime endDate; // Change OffsetDateTime to LocalDateTime

    @Column(name = "user_id")
    private Long userId;

    /**
     * Получить идентификатор финансовой цели.
     * Get the ID of the financial goal.
     */
    public Long getId() {
        return id;
    }

    /**
     * Установить идентификатор финансовой цели.
     * Set the ID of the financial goal.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получить название финансовой цели.
     * Get the name of the financial goal.
     */
    public String getName() {
        return name;
    }

    /**
     * Установить название финансовой цели.
     * Set the name of the financial goal.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получить сумму финансовой цели.
     * Get the sum of the financial goal.
     */
    public BigDecimal getSum() {
        return sum;
    }

    /**
     * Установить сумму финансовой цели.
     * Set the sum of the financial goal.
     */
    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    /**
     * Получить дату начала финансовой цели.
     * Get the start date of the financial goal.
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * Установить дату начала финансовой цели.
     * Set the start date of the financial goal.
     */
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    /**
     * Получить дату окончания финансовой цели.
     * Get the end date of the financial goal.
     */
    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     * Установить дату окончания финансовой цели.
     * Set the end date of the financial goal.
     */
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    /**
     * Получить идентификатор пользователя, связанного с финансовой целью.
     * Get the ID of the user associated with the financial goal.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Установить идентификатор пользователя, связанного с финансовой целью.
     * Set the ID of the user associated with the financial goal.
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
