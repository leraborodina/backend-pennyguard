package ru.itcolleg.transaction.model;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "[transaction]", schema = "[dbo]")
public class Transaction {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name="user_id")
    private Long userId;

    @Column(name="category_id")
    private Long categoryId;

    @Column(name="transaction_type_id")
    private Long transactionTypeId;

    private LocalDate date;

    private Double amount;

    private String purpose;

    private Boolean regular;

    public Transaction(){

    }

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
}
