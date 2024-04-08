package ru.itcolleg.transaction.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionDTO {
    private Long id;
    private Long categoryId;
    private Long transactionTypeId;
    private LocalDate date;
    private Double amount;
    private String purpose;
    private Boolean regular;

    public TransactionDTO (){

    }

    public TransactionDTO(Long categoryId, Long transactionTypeId, LocalDate date, Double amount, String purpose, Boolean regular) {
        this.categoryId = categoryId;
        this.transactionTypeId = transactionTypeId;
        this.date = date;
        this.amount = amount;
        this.purpose = purpose;
        this.regular = regular;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Long transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Boolean getRegular() {
        return regular;
    }

    public void setRegular(Boolean regular) {
        this.regular = regular;
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", transactionTypeId=" + transactionTypeId +
                ", date=" + date +
                ", amount=" + amount +
                ", purpose='" + purpose + '\'' +
                ", regular=" + regular +
                '}';
    }
}
