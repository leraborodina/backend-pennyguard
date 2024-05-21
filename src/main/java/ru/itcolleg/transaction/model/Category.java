package ru.itcolleg.transaction.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

/**
 * Сущность категории транзакции.
 * Entity class representing a transaction category.
 */
@Entity
@Table(name = "[category]", schema = "[dbo]", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "user_id"})})
public class Category {

    private static final Logger logger = LoggerFactory.getLogger(Category.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_default")
    private boolean isDefault;

    @Column(name = "user_id")
    private Long userId;

    /**
     * Получить идентификатор категории.
     * Get the category ID.
     *
     * @return The category ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Установить идентификатор категории.
     * Set the category ID.
     *
     * @param id The category ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получить имя категории.
     * Get the category name.
     *
     * @return The category name
     */
    public String getName() {
        return name;
    }

    /**
     * Установить имя категории.
     * Set the category name.
     *
     * @param name The category name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Проверить, является ли категория по умолчанию.
     * Check if the category is default.
     *
     * @return True if the category is default, false otherwise
     */
    public boolean isDefault() {
        return isDefault;
    }

    /**
     * Установить, является ли категория по умолчанию.
     * Set whether the category is default.
     *
     * @param aDefault True if the category is default, false otherwise
     */
    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
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
}
