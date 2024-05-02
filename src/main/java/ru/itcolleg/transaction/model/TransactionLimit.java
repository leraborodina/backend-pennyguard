package ru.itcolleg.transaction.model;

import javax.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "[limit]", schema = "[dbo]")
public class TransactionLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "category_id", nullable = true)
    private Long categoryId;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "salary_day", nullable = false)
    private Integer salaryDay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getSalaryDay() {
        return salaryDay;
    }

    public void setSalaryDay(Integer salaryDay) {
        this.salaryDay = salaryDay;
    }
}
