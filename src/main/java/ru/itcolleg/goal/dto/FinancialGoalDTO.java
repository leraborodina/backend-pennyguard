package ru.itcolleg.goal.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime; // Change OffsetDateTime to LocalDateTime

/**
 * DTO объект для финансовой цели.
 * DTO object for a financial goal.
 */
public class FinancialGoalDTO {
    private Long id;
    private String name;
    private BigDecimal sum;
    private LocalDateTime startDate; // Change OffsetDateTime to LocalDateTime
    private Integer endDate;
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Integer getEndDate() {
        return endDate;
    }

    public void setEndDate(Integer endDate) {
        this.endDate = endDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
