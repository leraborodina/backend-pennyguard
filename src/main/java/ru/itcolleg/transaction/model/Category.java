package ru.itcolleg.transaction.model;

import javax.persistence.*;

@Entity
@Table(name = "[category]", schema = "[dbo]", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "user_id"})})
public class Category {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name = "is_default")
    private boolean isDefault;

    @Column(name = "user_id")
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

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
