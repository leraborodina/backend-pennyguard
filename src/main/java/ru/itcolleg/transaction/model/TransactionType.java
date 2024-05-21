package ru.itcolleg.transaction.model;

import javax.persistence.*;

/**
 * Сущность типа транзакции.
 * Entity class representing a transaction type.
 */
@Entity
@Table(name = "[transaction_type]", schema = "[dbo]")
public class TransactionType {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "type")
    private String type;

    /**
     * Получить идентификатор типа транзакции.
     * Get the transaction type ID.
     *
     * @return The transaction type ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Установить идентификатор типа транзакции.
     * Set the transaction type ID.
     *
     * @param id The transaction type ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получить тип транзакции.
     * Get the transaction type.
     *
     * @return The transaction type
     */
    public String getType() {
        return type;
    }

    /**
     * Установить тип транзакции.
     * Set the transaction type.
     *
     * @param type The transaction type to set
     */
    public void setType(String type) {
        this.type = type;
    }
}
