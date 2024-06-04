package ru.itcolleg.transaction.dto;

/**
 * DTO class representing a transaction limit.
 * Класс DTO, представляющий лимит транзакции.
 */
public class CategoryLimitDTO {

    private Long id;
    private Long categoryId;
    private Double amount;
    private Integer startDay;

    /**
     * Retrieves the ID of the transaction limit.
     * Возвращает идентификатор лимита транзакции.
     *
     * @return The ID of the transaction limit
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the transaction limit.
     * Устанавливает идентификатор лимита транзакции.
     *
     * @param id The ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieves the category ID associated with the transaction limit.
     * Возвращает идентификатор категории, связанный с лимитом транзакции.
     *
     * @return The category ID
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the category ID associated with the transaction limit.
     * Устанавливает идентификатор категории, связанный с лимитом транзакции.
     *
     * @param categoryId The category ID to set
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Retrieves the amount set for the transaction limit.
     * Возвращает сумму, установленную для лимита транзакции.
     *
     * @return The amount of the transaction limit
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Sets the amount for the transaction limit.
     * Устанавливает сумму лимита транзакции.
     *
     * @param amount The amount to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Retrieves the salary day associated with the transaction limit.
     * Возвращает день зарплаты, связанный с лимитом транзакции.
     *
     * @return The salary day
     */
    public Integer getStartDay() {
        return startDay;
    }

    /**
     * Sets the salary day associated with the transaction limit.
     * Устанавливает день зарплаты, связанный с лимитом транзакции.
     *
     * @param startDay The salary day to set
     */
    public void setStartDay(Integer startDay) {
        this.startDay = startDay;
    }
}
