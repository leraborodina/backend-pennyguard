package ru.itcolleg.transaction.dto;

/**
 * DTO class representing a financial transaction.
 * Класс DTO, представляющий финансовую транзакцию.
 */
public class Transaction {
    private String date;
    private String time;
    private String description;
    private double amount;

    /**
     * Constructs a Transaction object with the specified attributes.
     * Создает объект транзакции с указанными атрибутами.
     *
     * @param date        The date of the transaction
     * @param time        The time of the transaction
     * @param description The description of the transaction
     * @param amount      The amount of the transaction
     */
    public Transaction(String date, String time, String description, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.amount = amount;
    }

    /**
     * Gets the date of the transaction.
     * Возвращает дату транзакции.
     *
     * @return The date of the transaction
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date of the transaction.
     * Устанавливает дату транзакции.
     *
     * @param date The date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the time of the transaction.
     * Возвращает время транзакции.
     *
     * @return The time of the transaction
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the time of the transaction.
     * Устанавливает время транзакции.
     *
     * @param time The time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Gets the description of the transaction.
     * Возвращает описание транзакции.
     *
     * @return The description of the transaction
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the transaction.
     * Устанавливает описание транзакции.
     *
     * @param description The description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the amount of the transaction.
     * Возвращает сумму транзакции.
     *
     * @return The amount of the transaction
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the transaction.
     * Устанавливает сумму транзакции.
     *
     * @param amount The amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
