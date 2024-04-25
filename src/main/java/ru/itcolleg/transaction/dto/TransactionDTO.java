package ru.itcolleg.transaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionDTO {
    private Long id;
    private Long userId;
    private Long categoryId;
    private Long typeId;
    private String createdAtStr;
    private Double amount;
    private String purpose;
    private Boolean regular;

    public TransactionDTO() {
    }

    public TransactionDTO(Long id, Long userId, Long categoryId, Long typeId, OffsetDateTime createdAt, Double amount, String purpose, Boolean regular) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.typeId = typeId;
        this.createdAtStr = createdAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
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

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getCreatedAtStr() {
        return createdAtStr;
    }

    public void setCreatedAtStr(String createdAtStr) {
        this.createdAtStr = createdAtStr;
    }

    public OffsetDateTime getCreatedAt() {
        return OffsetDateTime.parse(createdAtStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAtStr = createdAt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
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


