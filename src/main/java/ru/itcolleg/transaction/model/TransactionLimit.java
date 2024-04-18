package ru.itcolleg.transaction.model;

import javax.persistence.*;

import java.util.Date;

@Entity
@Table(name = "[limit]", schema = "[dbo]")
public class TransactionLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "limit_type", referencedColumnName = "limit_type")
    private TransactionLimitType limitType; // day, week, month

    @Column(name = "limit_value")
    private Double limitValue;

    @Column(name = "createdAt")
    private Date createdAt; // Date when the limit was set

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

    public TransactionLimitType getLimitType() {
        return limitType;
    }

    public void setLimitType(TransactionLimitType limitType) {
        this.limitType = limitType;
    }

    public Double getLimitValue() {
        return limitValue;
    }

    public void setLimitValue(Double limitValue) {
        this.limitValue = limitValue;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
