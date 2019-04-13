package org.yourotherleft.stockexchange.persistence.entity;

import org.yourotherleft.stockexchange.persistence.type.TransactionType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * An entity describing a transaction.
 *
 * @author jallen
 */
@Entity
@Table(name = "stock_transaction")
public class StockTransaction {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true, nullable = false)
    private String identifier;

    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private String stockSymbol;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double shares;

    /**
     * Zero-argument constructor needed by the JPA spec.
     */
    public StockTransaction() {
        // nothing to do
    }

    /**
     * Getter.
     *
     * @return The id (primary key) of this entity.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Getter.
     *
     * @return A unique identifier for this transaction.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Setter.
     *
     * @param identifier A unique identifier for this transaction.
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Getter.
     *
     * @return The {@link TransactionType} of this transaction.
     */
    public TransactionType getTransactionType() {
        return transactionType;
    }

    /**
     * Setter.
     *
     * @param transactionType The {@link TransactionType} of this transaction.
     */
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * Getter.
     *
     * @return The stock symbol for this transaction.
     */
    public String getStockSymbol() {
        return stockSymbol;
    }

    /**
     * Setter.
     *
     * @param stockSymbol The stock symbol for this transaction.
     */
    public void setStockSymbol(String stockSymbol) {
        // normalize stock symbols
        this.stockSymbol = stockSymbol.toUpperCase();
    }

    /**
     * Getter.
     *
     * @return The currency type (e.g., "USD", or US Dollars) for this transaction.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Setter.
     *
     * @param currency The currency type (e.g., "USD", or US Dollars) for this transaction.
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Getter.
     *
     * @return The amount of currency for this transaction.
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Setter.
     *
     * @param amount The amount of currency for this transaction.
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Getter.
     *
     * @return The number of shares represented in this transaction.
     */
    public Double getShares() {
        return shares;
    }

    /**
     * Setter.
     *
     * @param shares The number of shares represented in this transaction.
     */
    public void setShares(Double shares) {
        this.shares = shares;
    }

    @Override
    public String toString() {
        return "StockTransaction{" +
                "id=" + id +
                ", identifier='" + identifier + '\'' +
                ", transactionType=" + transactionType +
                ", stockSymbol='" + stockSymbol + '\'' +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                ", shares=" + shares +
                '}';
    }
}
