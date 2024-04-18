package ru.itcolleg.transaction.model;

import javax.persistence.*;

@Entity
@Table(name = "[limit_type]", schema = "[dbo]")
public class TransactionLimitType {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(name = "limit_type")
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}