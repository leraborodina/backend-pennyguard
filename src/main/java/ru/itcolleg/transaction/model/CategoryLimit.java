package ru.itcolleg.transaction.model;

import javax.persistence.*;

/**
 * Сущность ограничения расходов для категорий.
 * Entity class representing a spending limit for categories.
 */
@Entity
@Table(name = "[category_limit]", schema = "[dbo]")
public class CategoryLimit {

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

    /**
     * Получить идентификатор ограничения расходов для категорий.
     * Get the spending limit ID for categories.
     *
     * @return The spending limit ID for categories
     */
    public Long getId() {
        return id;
    }

    /**
     * Установить идентификатор ограничения расходов для категорий.
     * Set the spending limit ID for categories.
     *
     * @param id The spending limit ID for categories to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получить идентификатор пользователя.
     * Get the user ID.
     *
     * @return The user ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Установить идентификатор пользователя.
     * Set the user ID.
     *
     * @param userId The user ID to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Получить идентификатор категории.
     * Get the category ID.
     *
     * @return The category ID
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * Установить идентификатор категории.
     * Set the category ID.
     *
     * @param categoryId The category ID to set
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Получить сумму ограничения расходов для категорий.
     * Get the amount of the spending limit for categories.
     *
     * @return The amount of the spending limit for categories
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Установить сумму ограничения расходов для категорий.
     * Set the amount of the spending limit for categories.
     *
     * @param amount The amount of the spending limit for categories to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Получить день зарплаты.
     * Get the salary day.
     *
     * @return The salary day
     */
    public Integer getSalaryDay() {
        return salaryDay;
    }

    /**
     * Установить день зарплаты.
     * Set the salary day.
     *
     * @param salaryDay The salary day to set
     */
    public void setSalaryDay(Integer salaryDay) {
        this.salaryDay = salaryDay;
    }
}
